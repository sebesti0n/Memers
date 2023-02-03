package com.example.memers

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    private var queue: RequestQueue? = null
    private var jsonobjectrequest: JsonObjectRequest? = null

    private val url = "https://meme-api.com/gimme/whole"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadMeme()
    }

    // ...
    private fun loadMeme() {
        // Instantiate the RequestQueue.
        val memeimageView: ImageView = findViewById(R.id.image)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        queue = Volley.newRequestQueue(this)

        jsonobjectrequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val url = response.getString("url")

                Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeimageView)
            },
            {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        )
        queue?.add(jsonobjectrequest)
// Add the request to the RequestQueue.
    }

    fun shareMeme(view: View) {
//         val myIntent =Intent(Intent.ACTION_SEND)
//        myIntent.setType("text/plain")
//        val body = "Your body here"
//        val sub = "Your Subject"
//          myIntent.putExtra(Intent.EXTRA_SUBJECT,sub)
//          myIntent.putExtra(Intent.EXTRA_TEXT,body)
//         startActivity(Intent.createChooser(myIntent, "Share Using"))
        val memeimageView: ImageView = findViewById(R.id.image)

        val bitmapDrawable: BitmapDrawable = memeimageView.getDrawable() as BitmapDrawable
        val bitmap: Bitmap = bitmapDrawable.getBitmap()
        shareImageandText(bitmap)

    }
//
    private fun shareImageandText(bitmap: Bitmap) {


        val uri: Uri = getmageToShare(bitmap)

        val intent = Intent(Intent.ACTION_SEND)


        intent.putExtra(Intent.EXTRA_STREAM, uri)


//        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image")


        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")


        intent.setType("image/png")

        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getmageToShare(bitmap: Bitmap): Uri {
        val imagefolder= File(getCacheDir(), "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file= File(imagefolder, "shared_image.png")
            val outputStream= FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.anni.shareimage.fileprovider", file)
        } catch (e:Exception) {
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
        }
        return uri!!
    }

    fun nextMeme(view: View) {

loadMeme()
    }
}


