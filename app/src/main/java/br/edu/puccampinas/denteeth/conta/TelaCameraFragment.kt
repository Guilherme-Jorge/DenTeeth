package br.edu.puccampinas.denteeth.conta

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.puccampinas.denteeth.classes.CustomResponse
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.databinding.FragmentTelaCameraBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TelaCameraFragment : Fragment() {

    private var _binding: FragmentTelaCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private var storageRef = Firebase.storage.getReference("perfis/")

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private lateinit var cameraSelector: CameraSelector

    private var imageCapture: ImageCapture? = null

    private lateinit var imgCaptureExecuter: ExecutorService

    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                //
            } else {
                Snackbar.make(
                    binding.root,
                    "Você não concedeu permissôes para usar a câmera.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTelaCameraBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        cameraProviderResult.launch(Manifest.permission.CAMERA)

        cameraProviderFuture = ProcessCameraProvider.getInstance(binding.root.context)
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        imgCaptureExecuter = Executors.newSingleThreadExecutor()

        startCamera()

        binding.btnDeixarDepois.setOnClickListener {
            findNavController().navigate(R.id.action_TelaCameraFragment_to_TermosDeUsoFragment)
        }

        binding.btnCapture.setOnClickListener {
            binding.btnCapture.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            takePhoto()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        imgCaptureExecuter.shutdown()
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({

            imageCapture = ImageCapture.Builder().build()

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.pvViewFinder.surfaceProvider)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)


            } catch (e: Exception) {
                Log.e("CameraPreview", "falha ao abrir a camera.")
            }

        }, ContextCompat.getMainExecutor(binding.root.context))

    }

    private fun takePhoto() {
        imageCapture?.let {

            auth = Firebase.auth
            val user = auth.currentUser

            val filename = "${user!!.uid}-foto-pefil"
            val file = File(requireActivity().externalMediaDirs[0], filename)

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecuter,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i(
                            "TelaCameraFragment",
                            "A imagem foi salva no diretório: ${file.toUri()}"
                        )
                        Snackbar.make(
                            binding.root,
                            "Imagem salva com sucesso: ${file.name}",
                            Snackbar.LENGTH_LONG
                        ).show()

                        val perfisRef = storageRef.child(filename)
                        perfisRef.putFile(file.toUri()).addOnSuccessListener {
                            perfisRef.downloadUrl.addOnSuccessListener { uri ->
                                updateUserPhoto(uri.toString(), user.uid).addOnCompleteListener(
                                    requireActivity()
                                ) { res ->
                                    if (res.result.status == "SUCCESS") {
                                        Snackbar.make(
                                            binding.root,
                                            "Foto salva com sucesso!",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        findNavController().navigate(R.id.action_TelaCameraFragment_to_TermosDeUsoFragment)
                                    }
                                }
                            }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            binding.root.context,
                            "Erro ao salvar foto.",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("CameraPreview", "Exceção ao gravar arquivo da foto: $exception")
                    }
                })

        }
    }

    private fun updateUserPhoto(uri: String, uid: String): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        val novosDados = hashMapOf(
            "uri" to uri,
            "uid" to uid
        )

        return functions.getHttpsCallable("adcionarFotoPerfil").call(novosDados)
            .continueWith { task ->
                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}