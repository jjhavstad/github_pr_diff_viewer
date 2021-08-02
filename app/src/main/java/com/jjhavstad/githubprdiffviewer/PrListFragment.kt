package com.jjhavstad.githubprdiffviewer

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jjhavstad.githubprdiffviewer.databinding.FragmentPrListBinding
import com.jjhavstad.githubprdiffviewer.databinding.PrListContentBinding
import com.jjhavstad.githubprdiffviewer.models.Pr

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class PrListFragment : Fragment() {

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat =
        ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
            if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            false
        }

    private var _binding: FragmentPrListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPrListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat)

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Pr
            val bundle = Bundle()
            bundle.putString(
                PrDetailFragment.ARG_ITEM_TITLE,
                item.title
            )
            bundle.putString(
                PrDetailFragment.ARG_ITEM_DIFF_URL,
                item.diffUrl
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as Pr
            Toast.makeText(
                v.context,
                "Context click of item " + item.title,
                Toast.LENGTH_LONG
            ).show()
            true
        }
        setupRecyclerView(recyclerView, onClickListener, onContextClickListener)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener
    ) {

        recyclerView.adapter = PrListRecyclerViewAdapter(
            onClickListener,
            onContextClickListener
        )
    }

    class PrListRecyclerViewAdapter(
        private val onClickListener: View.OnClickListener,
        private val onContextClickListener: View.OnContextClickListener
    ) : ListAdapter<Pr, PrListRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                PrListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            getItem(position)?.let { item ->
                holder.contentView.text = item.title

                with(holder.itemView) {
                    tag = item
                    setOnClickListener(onClickListener)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setOnContextClickListener(onContextClickListener)
                    }
                }
            }
        }

        inner class ViewHolder(binding: PrListContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val contentView: TextView = binding.content
        }

        companion object {
            val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Pr>() {
                override fun areItemsTheSame(oldItem: Pr, newItem: Pr): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Pr, newItem: Pr): Boolean {
                    return oldItem.equals(newItem)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}