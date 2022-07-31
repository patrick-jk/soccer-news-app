package me.dio.soccernews.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import me.dio.soccernews.App
import me.dio.soccernews.R
import me.dio.soccernews.data.remote.SoccerNewsApiRepository
import me.dio.soccernews.databinding.FragmentNewsBinding
import me.dio.soccernews.ui.adapters.NewsAdapter

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel> {
        NewsViewModelFactory(
            SoccerNewsApiRepository.getInstance(), (activity?.application as App).localRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.srlNews.setOnRefreshListener(viewModel::getNews)
        viewModel.getNews()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.news.observe(viewLifecycleOwner) {
            when (it) {
                NewsViewModel.State.Loading -> binding.srlNews.isRefreshing = true
                is NewsViewModel.State.Error -> {
                    binding.srlNews.isRefreshing = false
                    Snackbar.make(binding.srlNews, R.string.txt_network_error, Snackbar.LENGTH_SHORT).show()
                }
                is NewsViewModel.State.Success -> {
                    binding.srlNews.isRefreshing = false
                    binding.rvNews.adapter = NewsAdapter(it.news, viewModel::saveNews)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}