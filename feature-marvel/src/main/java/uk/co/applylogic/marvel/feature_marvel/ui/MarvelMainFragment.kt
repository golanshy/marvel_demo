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
import uk.co.applylogic.marvel.core_android.listeners.OnBottomReachedListener
import uk.co.applylogic.marvel.data.utils.NetworkUtils


class MarvelMainFragment : Fragment() {

    internal lateinit var viewModelMarvel: MarvelMainViewModel
    private lateinit var binding: MainFragmentBinding
    val srlRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
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

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        viewModelMarvel.uiState.observe(viewLifecycleOwner, Observer { uiState ->
            srlRefreshing.value = false
            pbVisibility.value = View.INVISIBLE
            noResultsVisibility.value = View.INVISIBLE

            when (uiState) {
                is UIState.Initialized -> searchResultsAdapter.resetData()
                is UIState.Refreshing -> srlRefreshing.value = true
                is UIState.InProgress -> pbVisibility.value = View.VISIBLE
                is UIState.OnResults -> rvVisibility.value = View.VISIBLE
                is UIState.NoResults -> noResultsVisibility.value = View.VISIBLE
                is UIState.Error -> {
                    (activity as MarvelMainActivity).comp.networkErrorHandler()
                        .show(activity, uiState.errorMessage)
                }
            }
        })

        searchResultsAdapter = MarvelResultsAdapter(viewModelMarvel, this, mutableListOf())
        searchResultsAdapter.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                activity?.let {
                    if (NetworkUtils.isConnected(it))
                        viewModelMarvel.getContent()
                    else
                        (activity as MarvelMainActivity).comp.networkErrorHandler()
                            .show(activity, getString(R.string.no_internet_available))

                }
            }
        })

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

        if (viewModelMarvel.searchResults.value?.size == 0)
            viewModelMarvel.getContent()


        viewModelMarvel.searchResults.observe(viewLifecycleOwner, Observer {
            searchResultsAdapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModelMarvel.selectedResult.value = null
    }
}
