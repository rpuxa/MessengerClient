package ru.rpuxa.messenger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_friends.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.textColor
import ru.rpuxa.messenger.R
import ru.rpuxa.messenger.view.adapters.FriendRequestAdapter
import ru.rpuxa.messenger.view.adapters.FriendsAdapter
import ru.rpuxa.messenger.view.dialogs.LoadingDialog
import ru.rpuxa.messenger.viewModel
import ru.rpuxa.messenger.viewmodel.FriendsViewModel

class FriendsFragment : Fragment() {

    private val viewModel: FriendsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_friends, container, false)

    override fun onResume() {
        super.onResume()
        viewModel.loadFriendsRequest()
        viewModel.loadAllFriends()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var dialog: LoadingDialog? = null

        friend_request_search.setOnClickListener {
            val login = friend_request_login.text!!.toString()
            viewModel.sendFriendRequest(login)
        }

        fun requestMsg(text: String, error: Boolean) {
            friend_request_msg.apply {
                isVisible = true
                textColor = context!!.resources.getColor(
                    if (error) android.R.color.holo_red_dark else android.R.color.holo_green_dark
                )
                setText(text)
            }
        }

        viewModel.requestStatus.observe(this) {
            when (it) {
                FriendsViewModel.RequestStatus.REQUEST_SENT -> {
                    requestMsg(getString(R.string.request_sent), false)
                }

                FriendsViewModel.RequestStatus.ACCOUNT_IS_NOT_FOUND -> {
                    requestMsg(getString(R.string.account_not_found), true)
                }

                FriendsViewModel.RequestStatus.ALREADY_SENT_REQUEST -> {
                    requestMsg(getString(R.string.already_sent_request), true)
                }

                FriendsViewModel.RequestStatus.ALREADY_IN_FRIENDS -> {
                    requestMsg(getString(R.string.already_in_friends), true)
                }

                FriendsViewModel.RequestStatus.CANT_SEND_REQUEST_TO_YOURSELF -> {
                    requestMsg(getString(R.string.cant_send_request_to_yourself), true)
                }

                FriendsViewModel.RequestStatus.SERVER_ERROR -> {
                    requestMsg(getString(R.string.unreachable_server), true)
                }
            }

            dialog?.dismiss()
            if (it == FriendsViewModel.RequestStatus.SENDING) {
                dialog =
                    LoadingDialog.show(childFragmentManager, getString(R.string.sending_request))
            }
        }

        viewModel.answerStatus.observe(this) {
            dialog?.dismiss()
            when (it) {
                FriendsViewModel.AnswerStatus.SERVER_ERROR -> {
                    longToast(R.string.unreachable_server)
                    viewModel.resetAnswerStatus()
                }

                FriendsViewModel.AnswerStatus.LOADING -> {
                    dialog = LoadingDialog.show(childFragmentManager, getString(R.string.loading))
                }
            }
        }

        val friendRequestAdapter = FriendRequestAdapter(this, viewModel)

        friend_requests_list.layoutManager = LinearLayoutManager(requireContext())
        friend_requests_list.adapter = friendRequestAdapter

        viewModel.friendRequests.observe(this) {
            friendRequestAdapter.submitList(it)
        }

        val friendsAdapter = FriendsAdapter(this)

        friends_list.layoutManager = LinearLayoutManager(requireContext())
        friends_list.adapter = friendsAdapter

        viewModel.friends.observe(this) {
            friendsAdapter.submitList(it)
        }
    }
}