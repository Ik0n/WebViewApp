package sportssisal.matchgame.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.*
import sportssisal.matchgame.R
import sportssisal.matchgame.adapters.NewsAdapter
import sportssisal.matchgame.databinding.FragmentNewsBinding
import sportssisal.matchgame.details.DetailsNewsFragment
import sportssisal.matchgame.utils.Resource

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel>()
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewModel.newsLiveData.observe(viewLifecycleOwner) { response ->

            when(response) {
                is Resource.Success -> {
                    pag_progress_bar.visibility = View.INVISIBLE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is  Resource.Error -> {
                    pag_progress_bar.visibility = View.INVISIBLE
                    response.data?.let {
                        Log.e("checkData", "NewsFragment: error: ${it}")
                    }
                }
                is  Resource.Loading -> {
                    pag_progress_bar.visibility = View.VISIBLE
                }
            }

        }


        newsAdapter.setOnItemClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.container, DetailsNewsFragment.newInstance(it))
                .commit()
        }

    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        news_adapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}