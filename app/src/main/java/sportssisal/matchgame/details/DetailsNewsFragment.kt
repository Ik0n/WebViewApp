package sportssisal.matchgame.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_atricle.*
import sportssisal.matchgame.MainActivity
import sportssisal.matchgame.OnBackPressed
import sportssisal.matchgame.R
import sportssisal.matchgame.databinding.FragmentDetailsNewsBinding
import sportssisal.matchgame.models.Article
import java.lang.Exception


@AndroidEntryPoint
class DetailsNewsFragment: Fragment(), OnBackPressed {

    private var _binding: FragmentDetailsNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailsNewsViewModel>()


    companion object {
        @JvmStatic
        fun newInstance(article: Article): Fragment {
            return DetailsNewsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("article", article)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsNewsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    @RequiresApi(33)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setOnBackPressedListener(this)

        var article = arguments?.getSerializable("article") as Article

        with(binding) {
            articleDetailsTitle.text = article?.title
            articleDetailsDescriptionText.text = article?.content

            Glide.with(requireContext()).load(article.urlToImage).placeholder(R.drawable.jpeg).into(headerImage)

            articleDetailsButton.setOnClickListener {
                try {
                    Intent()
                        .setAction(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(takeIf { URLUtil.isValidUrl(article.url) }
                            ?.let {
                                article.url
                            }?: "https://google.com" ))
                        .let {
                            ContextCompat.startActivity(requireContext(), it, null)
                        }
                } catch (e: Exception) {
                    Toast.makeText(context, "The device doesn't have any browser to view the document!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onClick() {
        parentFragmentManager.popBackStack()
    }


}