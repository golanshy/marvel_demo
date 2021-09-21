package uk.co.applylogic.marvel.feature_marvel.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.applylogic.marvel.data.model.UIState
import uk.co.applylogic.marvel.feature_marvel.R
import uk.co.applylogic.marvel.feature_marvel.databinding.MainFragmentBinding
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.flow.collectLatest

class MarvelMainFragment : Fragment() {

    private lateinit var viewModelMarvel: MarvelMainViewModel
    private lateinit var binding: MainFragmentBinding
    val srlRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelMarvel = ViewModelProvider(this)[MarvelMainViewModel::class.java]
        viewModelMarvel.comp = (activity as MarvelMainActivity).comp

        binding.lifecycleOwner = this
        binding.view = this
        binding.viewModel = viewModelMarvel

        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        viewModelMarvel.uiState.observe(viewLifecycleOwner, Observer { uiState ->
            srlRefreshing.value = false
            noResultsVisibility.value = View.INVISIBLE

            when (uiState) {
                is UIState.Initialized -> {}
                is UIState.Refreshing -> {}
                is UIState.InProgress -> {}
                is UIState.OnResults -> {}
                is UIState.NoResults -> noResultsVisibility.value = View.VISIBLE
                is UIState.Error -> {
                    (activity as MarvelMainActivity).comp.networkErrorHandler()
                        .show(activity, uiState.errorMessage)
                }
            }
        })

        searchResultsAdapter = MarvelResultsAdapter()

        binding.rvSearchResults.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            val dividerItemDecoration = DividerItemDecoration(
                activity,
                (layoutManager as LinearLayoutManager).orientation
            )
            adapter = searchResultsAdapter
            addItemDecoration(dividerItemDecoration)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModelMarvel.searchResultsFlow.collectLatest { pagingData ->
                searchResultsAdapter.submitData(pagingData)
            }
        }
    }
}
