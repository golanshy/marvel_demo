package uk.co.applylogic.marvel.feature_marvel.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_fragment.*
import uk.co.applylogic.marvel.data.model.UIState
import uk.co.applylogic.marvel.feature_marvel.R
import uk.co.applylogic.marvel.feature_marvel.databinding.MainFragmentBinding
import androidx.recyclerview.widget.DividerItemDecoration

class MarvelMainFragment : Fragment() {

    private lateinit var viewModelMarvel: MarvelMainViewModel
    private lateinit var binding: MainFragmentBinding
    var pbVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    var rvVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    var noResultsVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)

    private lateinit var searchResultsAdapter: MarvelResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelMarvel = ViewModelProviders.of(this).get(MarvelMainViewModel::class.java)
        viewModelMarvel.comp = (activity as MarvelMainActivity).comp

        binding.lifecycleOwner = this
        binding.view = this
        binding.viewModel = viewModelMarvel

        viewModelMarvel.uiState.observe(this, Observer { uiState ->
            pbVisibility.value = View.INVISIBLE
            rvVisibility.value = View.INVISIBLE
            noResultsVisibility.value = View.INVISIBLE

            when (uiState) {
                is UIState.Initialized -> {
                }
                is UIState.InProgress -> pbVisibility.value = View.VISIBLE
                is UIState.OnResults -> rvVisibility.value = View.VISIBLE
                is UIState.NoResults -> noResultsVisibility.value = View.VISIBLE
                is UIState.Error -> {
                    (activity as MarvelMainActivity).comp.networkErrorHandler()
                        .show(activity, uiState.errorMessage)
                }
            }
        })

        searchResultsAdapter = MarvelResultsAdapter(this, mutableListOf())

        rvSearchResults.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            val dividerItemDecoration = DividerItemDecoration(
                activity,
                (layoutManager as LinearLayoutManager).orientation
            )
            adapter = searchResultsAdapter
            addItemDecoration(dividerItemDecoration)
        }

        viewModelMarvel.searchResults.observe(this, Observer { searchResults ->
            searchResults?.let {
                searchResultsAdapter.notifyDataSetChanged(it)
            }
        })

        viewModelMarvel.getContent(null)
    }

}
