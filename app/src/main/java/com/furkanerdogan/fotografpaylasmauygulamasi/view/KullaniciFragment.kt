package com.furkanerdogan.fotografpaylasmauygulamasi.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.furkanerdogan.fotografpaylasmauygulamasi.databinding.FragmentKullaniciBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class KullaniciFragment : Fragment() {

    private var _binding: FragmentKullaniciBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kayitButton.setOnClickListener { kayitOl(it) }
        binding.girisButton.setOnClickListener { girisYap(it) }


        val guncelKullanici = auth.currentUser
        if(guncelKullanici != null) {

            //kullanici daha önceden giriş yapmış
            val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }



    fun kayitOl(view: View) {

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Kullanıcı oluşturuldu, navigasyonu yap
                        val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }.addOnFailureListener { exception ->
                    AlertDialog.Builder(requireContext())
                        .setTitle("Kayıt Başarısız")
                        .setMessage(exception.localizedMessage)
                        .setPositiveButton("Tamam") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
        } else {
            // Email veya şifre boşsa da bir uyarı verebiliriz
            AlertDialog.Builder(requireContext())
                .setTitle("Eksik Bilgi")
                .setMessage("Lütfen e-posta ve şifre alanlarını doldurunuz.")
                .setPositiveButton("Tamam") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }




    fun girisYap(view: View) {


        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { task ->

                    // Kullanıcı giriş yaptı, navigasyonu yap
                    val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)

            }.addOnFailureListener { exception ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Giriş Başarısız")
                    .setMessage(exception.localizedMessage)
                    .setPositiveButton("Tamam") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
        } else {
            // Email veya şifre boşsa da bir uyarı verebiliriz
            AlertDialog.Builder(requireContext())
                .setTitle("Eksik Bilgi")
                .setMessage("Lütfen e-posta ve şifre alanlarını doldurunuz.")
                .setPositiveButton("Tamam") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    
}