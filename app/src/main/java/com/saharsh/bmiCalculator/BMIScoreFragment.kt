package com.saharsh.bmiCalculator

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bmi_fragment.*
import java.io.*
import java.lang.Exception
import java.util.*


class BMIScoreFragment(value: Double, context: Context) : BottomSheetDialogFragment()
{

    val result = value
    private var fragmentView: View? = null
    var contexts = context



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         fragmentView = inflater.inflate(R.layout.bmi_fragment, container,false)
        init()
        return fragmentView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init(){
        val bottomSheet: FrameLayout? = fragmentView?.findViewById(R.id.bottomsheets)
        /*bottomSheet?.getLayoutParams()?.height = ViewGroup.LayoutParams.MATCH_PARENT
*/
        var bmi_count_tv: TextView? = fragmentView?.findViewById(R.id.bmi_score_tv)
        var bmi_title_tv: TextView? = fragmentView?.findViewById(R.id.bmi_title_tv)

        var underweight_tv: TextView? = fragmentView?.findViewById(R.id.underweight_tv)
        var underweight_count_tv: TextView? = fragmentView?.findViewById(R.id.underweight_count_tv)
        underweight_count_tv?.setText("> 18.5")

        var normal_tv: TextView? = fragmentView?.findViewById(R.id.normal_tv)
        var normal_count_tv: TextView? = fragmentView?.findViewById(R.id.normal_count_tv)

        var overweight_tv: TextView? = fragmentView?.findViewById(R.id.overweight_tv)
        var overweight_count_tv: TextView? = fragmentView?.findViewById(R.id.overweight_count_tv)

        var obese_tv: TextView? = fragmentView?.findViewById(R.id.obese_tv)
        var obese_count_tv: TextView? = fragmentView?.findViewById(R.id.obese_count_tv)

        var severely_obese_tv: TextView? = fragmentView?.findViewById(R.id.severely_obese_tv)
        var severely_obese_count_tv: TextView? = fragmentView?.findViewById(R.id.severely_obese_count_tv)

        var very_severely_obese_tv: TextView? = fragmentView?.findViewById(R.id.very_severely_obese_tv)
        var very_severely_obese_count_tv: TextView? = fragmentView?.findViewById(R.id.very_severely_obese_count_tv)
        very_severely_obese_count_tv?.setText("< 40")

        bmi_count_tv?.setText(result.toString().substring(0,
            result.toString().indexOf('.')+2
        ))

        when(result){
            in 0.0..18.5 -> {
                underweight_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                underweight_count_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                underweight_tv?.setTypeface(null, Typeface.BOLD)
                underweight_count_tv?.setTypeface(null, Typeface.BOLD)

                bmi_title_tv?.setText(underweight_tv?.text.toString()+"!")
            }
            in 18.5..25.0 -> {
                normal_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                normal_count_tv?.setTextColor(contexts.getColor(R.color.green_tv))

                normal_tv?.setTypeface(null, Typeface.BOLD)
                normal_count_tv?.setTypeface(null, Typeface.BOLD)

                bmi_title_tv?.setText(normal_tv?.text.toString()+"!")

            }
            in 25.0..30.0 -> {
                overweight_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                overweight_count_tv?.setTextColor(contexts.getColor(R.color.green_tv))

                overweight_tv?.setTypeface(null, Typeface.BOLD)
                overweight_count_tv?.setTypeface(null, Typeface.BOLD)

                bmi_title_tv?.setText(overweight_tv?.text.toString()+"!")

            }
            in 30.0..35.0 -> {
                obese_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                obese_count_tv?.setTextColor(contexts.getColor(R.color.green_tv))

                obese_tv?.setTypeface(null, Typeface.BOLD)
                obese_count_tv?.setTypeface(null, Typeface.BOLD)

                bmi_title_tv?.setText(obese_tv?.text.toString()+"!")

            }
            in 35.0..40.0 -> {
                severely_obese_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                severely_obese_count_tv?.setTextColor(contexts.getColor(R.color.green_tv))

                severely_obese_tv?.setTypeface(null, Typeface.BOLD)
                severely_obese_count_tv?.setTypeface(null, Typeface.BOLD)

                bmi_title_tv?.setText(severely_obese_tv?.text.toString()+"!")

            }
            in 40.0..Double.MAX_VALUE -> {
                very_severely_obese_tv?.setTextColor(contexts.getColor(R.color.green_tv))
                very_severely_obese_count_tv?.setTextColor(contexts.getColor(R.color.green_tv))

                very_severely_obese_tv?.setTypeface(null, Typeface.BOLD)
                very_severely_obese_count_tv?.setTypeface(null, Typeface.BOLD)

                bmi_title_tv?.setText(very_severely_obese_tv?.text.toString()+"!")

            }

        }

        var saveImage:TextView? = fragmentView?.findViewById(R.id.saveImage)
        saveImage?.setOnClickListener(View.OnClickListener {
            var bitmap  = convertImage(bottomsheets)
            saveImageToExternalStorage(bitmap)
        })

        var shareImage:TextView? = fragmentView?.findViewById(R.id.shareImage)
        shareImage?.setOnClickListener(View.OnClickListener {
            shareImage?.visibility = View.INVISIBLE
            saveImage?.visibility = View.INVISIBLE
            shareImageUri()

            shareImage?.visibility = View.VISIBLE
            saveImage?.visibility = View.VISIBLE
        })

    }

    fun convertImage(view: View): Bitmap {
        var bitmap: Bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

   /* fun saveBitmap(){

        var root: String = Environment.getRootDirectory().absolutePath
        var file = File(root+"/bmi_calculator")
        file.mkdirs()

        var myDir = File(file, "mybmi.jpeg")
        if(myDir.exists())
        { myDir.delete() }

        try {
            var fileOutputStream = FileOutputStream(myDir)
            convertImage(bottomsheets).compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            Toast.makeText(contexts,"Image has been saved!!",Toast.LENGTH_SHORT).show()
            fileOutputStream.flush()
            fileOutputStream.close()
        }
        catch (obj : Exception){
            obj.printStackTrace()
        }


    }
*/
    fun getImageUri(inImage:Bitmap): Uri{
        var bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var path = MediaStore.Images.Media.insertImage(contexts.contentResolver,inImage,"mybmi",null)
        return Uri.parse(path)
    }

    fun shareImageUri(){
        var intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, getImageUri(convertImage(bottomsheets)))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setType("image/jpeg")
        startActivity(intent)
    }

    // Method to save an image to external storage
    private fun saveImageToExternalStorage(bitmap:Bitmap):Uri{
        // Get the external storage directory path
        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image
        val file = File(path, "mybmi.jpg")
        if(file.exists()){
            file.delete()
        }

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()
            toast("Image saved successful.")
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
            toast("Error to save image.")
        }

        // Return the saved image path to uri
        return Uri.parse(file.absolutePath)
    }

    // Extension function to show toast message
    fun toast(message: String) {
        Toast.makeText(contexts, message, Toast.LENGTH_SHORT).show()
    }
}
