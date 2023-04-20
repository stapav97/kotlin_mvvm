package com.example.kotlin_mvvm_app.ui.first

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.FirstFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.ui.base.newViewModelWithArgs
import com.example.kotlin_mvvm_app.utils.binding.viewBinding


class FirstFragment : BaseFragment(R.layout.first_fragment) {

    private val binding: FirstFragmentBinding by viewBinding(FirstFragmentBinding::bind)
    private lateinit var mViewModel: FirstViewModel
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun initUI() {
        super.initUI()
        mViewModel = newViewModelWithArgs()

        requestPermissionLauncher = initializeRequestPermissionLauncher()

        binding.crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        binding.permissionButton.setOnClickListener {
            askNotificationPermission()
        }

        mViewModel.observeCommands(viewLifecycleOwner, this)
    }

    private fun initializeRequestPermissionLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // TODO permission granted
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // TODO permission deny forever, open application settings
                    Toast.makeText(requireContext(), "Deny forever", Toast.LENGTH_LONG).show()
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", requireContext().packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)


                } else {
                    // TODO permission deny once
                    Toast.makeText(requireContext(), "Deny once", Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    //TODO permission was granted previously
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    //TODO permission ad denied once
                    showDefaultDialog(requireContext())
                }
                else -> {
                    //TODO request permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun showDefaultDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.apply {
            setTitle("Notification permission")
            setMessage("Need for sent you notification")
            setPositiveButton("Positive") { _: DialogInterface?, _: Int ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            setNegativeButton("No, thanks") { _, _ ->
            }
        }.create().show()
    }
}

