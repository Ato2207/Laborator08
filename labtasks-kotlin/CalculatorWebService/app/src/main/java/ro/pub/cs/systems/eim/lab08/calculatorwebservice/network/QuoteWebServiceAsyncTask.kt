package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network

import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants
import java.lang.ref.WeakReference

class QuoteWebServiceAsyncTask(private val quoteTextView: TextView) : AsyncTask<Void, Void, String>() {
    private val quoteTextViewReference: WeakReference<TextView> = WeakReference(quoteTextView)

    override fun doInBackground(vararg params: Void?): String? {
        val client = okhttp3.OkHttpClient()
        var request: Request? = null

        try {
            request = Request.Builder()
                .url("https://dummyjson.com/quotes")
                .build()

            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful && response.body != null) {
                val json = JSONObject(response.body!!.string())
                val quotesArray = json.getJSONArray("quotes")
                val first = quotesArray.getJSONObject(0)
                val quoteText = first.getString("quote")

                return quoteText
            } else {
                return "Error: ${response.code} ${response.message}"
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "OkHttp request failed: ${e.message}")
            return "Error: ${e.message}"
        }
    }

    override fun onPostExecute(result: String?) {
        val quoteTextView = quoteTextViewReference.get()
        quoteTextView?.text = result
    }
}
