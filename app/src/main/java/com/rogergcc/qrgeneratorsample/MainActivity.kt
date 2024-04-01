package com.rogergcc.qrgeneratorsample

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.rogergcc.qrgeneratorsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGenerateQRCode.setOnClickListener {
            val text = binding.etText.text.toString()
            try {
                if (text.isEmpty()) {
                    Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val bitmap = encodeAsBitmap(text)

                binding.ivQRCode.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()

            }
            binding.etText.visibility = android.view.View.GONE

            binding.ivQRCode.setOnClickListener {
                binding.etText.visibility = android.view.View.VISIBLE
            }
        }


    }


    @Throws(WriterException::class)
    fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        val bitMatrix: BitMatrix = QRCodeWriter().encode(
            text,
            BarcodeFormat.QR_CODE,
            width,
            height
        )
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    fun encodeAsBitmap(str: String): Bitmap? {
        try {

            val result: BitMatrix
            try {
                result = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 500, 500)
            } catch (iae: IllegalArgumentException) {
                // Unsupported format
                return null
            }
            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result[x, y]) Color.BLACK else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}