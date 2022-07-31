package me.dio.soccernews.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import me.dio.soccernews.App
import me.dio.soccernews.databinding.FragmentFavoritesBinding
import me.dio.soccernews.ui.adapters.NewsAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(((activity?.application as App).localRepository))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun loadFavoriteNews() {
        viewModel.favoriteNews.observe(viewLifecycleOwner) {
            binding.rvNews.adapter = NewsAdapter(it) { favoriteNews ->
                viewModel.save(favoriteNews)
                loadFavoriteNews()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadFavoriteNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}