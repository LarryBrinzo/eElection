package com.eelection.Home.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eelection.CandidatesDataClass
import com.eelection.Home.Adapters.CandidatesAdapter
import com.eelection.Home.Adapters.ElectionAdapter

import com.eelection.R
import com.google.firebase.database.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ElectionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ElectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ElectionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    var cadidateslist: MutableList<CandidatesDataClass> = ArrayList()
    lateinit var electionAdapter : ElectionAdapter
    lateinit var candidatesrecycle : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View = inflater.inflate(R.layout.frag_election, container, false)

        candidatesrecycle = view.findViewById(R.id.candidatesrecycle)

        electionAdapter = ElectionAdapter(cadidateslist, context)
        val recycepost = GridLayoutManager(context, 1)
        candidatesrecycle.setLayoutManager(recycepost)
        recycepost.isAutoMeasureEnabled = false
        candidatesrecycle.setItemAnimator(DefaultItemAnimator())
        candidatesrecycle .setAdapter(electionAdapter)

        nearCandidates()

        return view
    }

    fun nearCandidates() {

        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Candidates")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                cadidateslist.clear()

                for (dataSnapshot1 in dataSnapshot.children) {

                    val candidate : CandidatesDataClass? = dataSnapshot1.getValue<CandidatesDataClass>(CandidatesDataClass::class.java)
                    candidate!!.CandidateNumber=dataSnapshot.key.toString()

                    cadidateslist.add(candidate)
                    electionAdapter.notifyDataSetChanged()

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }




    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ElectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ElectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
