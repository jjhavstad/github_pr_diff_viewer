package com.jjhavstad.githubprdiffviewer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jjhavstad.githubprdiffviewer.databinding.FragmentPrDetailBinding
import com.jjhavstad.githubprdiffviewer.databinding.PrDetailContentBinding
import com.jjhavstad.githubprdiffviewer.ext.applyBackgroundColorToRegion
import com.jjhavstad.githubprdiffviewer.models.PrDiffSplit
import com.jjhavstad.githubprdiffviewer.util.PrDiffSplitParser
import com.jjhavstad.githubprdiffviewer.viewmodels.PrDiffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PrListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class PrDetailFragment : Fragment() {
    private val prDiffViewModel: PrDiffViewModel by viewModel()

    private val weakThis: WeakReference<PrDetailFragment> = WeakReference(this)

    private var _binding: FragmentPrDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var prTitle: String? = null
    private var prDiffUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_TITLE)) {
                prTitle = it.getString(ARG_ITEM_TITLE)
            }
            if (it.containsKey(ARG_ITEM_DIFF_URL)) {
                prDiffUrl = it.getString(ARG_ITEM_DIFF_URL)
            }
        }

        prDiffViewModel.prDiffSplitLiveData.observe(this) {
            weakThis.get()?.apply {
                it?.let {
                    ((binding.itemDetail as RecyclerView).adapter as PrDetailRecyclerViewAdapter).submitList(
                        it.prSplits
                    )
                }
            }
        }
        prDiffUrl?.let {
            prDiffViewModel.requestPrDiffSplits(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPrDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.toolbarLayout?.title = prTitle

        setupRecyclerView(binding.itemDetail as RecyclerView)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        return rootView
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView) {
        recyclerView.adapter = PrDetailRecyclerViewAdapter()
    }

    class PrDetailRecyclerViewAdapter :
        ListAdapter<PrDiffSplit.PrSplit, PrDetailRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK)
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                PrDetailContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            getItem(position)?.let { item ->
                holder.addLines.text = item.add.applyBackgroundColorToRegion(
                    ContextCompat.getColor(holder.addLines.context, android.R.color.holo_green_light),
                    '+',
                    '\n'
                )
                holder.deleteLines.text = item.remove.applyBackgroundColorToRegion(
                    ContextCompat.getColor(holder.deleteLines.context, android.R.color.holo_red_light),
                    '-',
                    '\n'
                )
            }
        }

        inner class ViewHolder(binding: PrDetailContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val addLines: TextView = binding.diffLinesAdded
            val deleteLines: TextView = binding.diffLinesDeleted
        }

        companion object {
            val DIFF_CALLBACK = object: DiffUtil.ItemCallback<PrDiffSplit.PrSplit>() {
                override fun areItemsTheSame(oldItem: PrDiffSplit.PrSplit, newItem: PrDiffSplit.PrSplit): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: PrDiffSplit.PrSplit, newItem: PrDiffSplit.PrSplit): Boolean {
                    return oldItem.equals(newItem)
                }
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_TITLE = "item_title"
        const val ARG_ITEM_DIFF_URL = "item_diff_url"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}