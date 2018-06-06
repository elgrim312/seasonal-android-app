package com.example.elgrim.seasonal.candidate

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.elgrim.seasonal.Constants
import com.example.elgrim.seasonal.R
import com.example.elgrim.seasonal.adapter.CandidateAdapterList
import com.example.elgrim.seasonal.http.APIController
import com.example.elgrim.seasonal.http.ServiceVolley
import com.example.elgrim.seasonal.model.Candidate
import com.example.elgrim.seasonal.model.Job
import com.example.elgrim.seasonal.utils.PreferenceHelper
import com.example.elgrim.seasonal.utils.PreferenceHelper.get
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.fragment_candidate_list.*
import org.json.JSONArray
import java.io.StringReader
import java.util.*


data class User(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String
)

data class Candidate2(
        val user: User,
        val year_exp: Int,
        val available_at: String,
        val profile_view_count: Int,
        val wage_claim: Int,
        val profile_picture: String? = null,
        val description: String,
        val job: Job,
        val job_id: Int
)

class CandidateFragmentList : Fragment() {
    //get any value from prefs

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        prefs = PreferenceHelper.defaultPrefs(this.activity)
        getCandidates()
        return inflater.inflate(R.layout.fragment_candidate_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val candidates = arrayListOf<Candidate>()
        professional_recycler_list.layoutManager = LinearLayoutManager(context)
        val itemAdapter = FastItemAdapter<CandidateAdapterList>()
        itemAdapter.add(candidates.map { CandidateAdapterList(it) })

        professional_recycler_list.adapter = itemAdapter
    }

    private fun getCandidates() {
        val service = ServiceVolley()
        val apiController = APIController(service)
        apiController.get("candidates/", prefs[Constants.TOKEN]) { response ->
            if (response != null) {
                //Log.d("res", response.toString())
            }
        }
        val json2 = """
    [{
        "user": {
            "id": 1,
            "email": "amyrodriguez@yahoo.com",
            "first_name": "Thomas",
            "last_name": "Heath"
        },
        "year_exp": 3,
        "available_at": "2013-05-11T10:54:20Z",
        "profile_view_count": 378,
        "wage_claim": 34357,
        "profile_picture": null,
        "description": "Low attack check nor play education.",
        "job": {
            "id": 1,
            "name": "serveur"
        },
        "job_id": 1
    }]
    """
        val result = Klaxon().parseArray<Candidate2>(json2)
        Log.d("LOL", result.toString())
    }
}

