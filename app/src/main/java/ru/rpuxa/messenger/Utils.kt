package ru.rpuxa.messenger

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import ru.rpuxa.messenger.viewmodel.ViewModelFactory
import java.util.*

val random = Random()


@Suppress("FunctionName")
fun <T> MutableLiveData(value: T) = MutableLiveData<T>().apply {
    setValue(value)
}

inline fun <T> MutableLiveData() = MutableLiveData<T>()

@Suppress("UNCHECKED_CAST")
fun <T> nullLiveData() = NullLiveData
        as LiveData<T?>

object NullLiveData : LiveData<Nothing?>() {
    init {
        postValue(null)
    }
}

var <T> MutableLiveData<T>.nnValue: T
    @Deprecated("Use value!!")
    inline get() = value!!
    set(value) {
        if (Looper.myLooper() == null) {
            postValue(value)
        } else {
            setValue(value)
        }
    }

inline fun <T> MutableLiveData<T>.update(block: T.() -> Unit = {}) {
    val v = value
    v!!.block()
    value = v
}

inline val Fragment.lazyNavController get() = lazy { findNavController() }

@Suppress("UNCHECKED_CAST")
fun <T> getEqualsDiff() = EqualsDiff as DiffUtil.ItemCallback<T>

private object EqualsDiff : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Any, newItem: Any) =
        areItemsTheSame(oldItem, newItem)
}

fun ViewGroup.inflate(@LayoutRes res: Int): View {
    val inflater = LayoutInflater.from(context)
    return inflater.inflate(res, this, false)
}

inline fun <reified VM : ViewModel> ComponentActivity.viewModel() = viewModels<VM>(::ViewModelFactory)
inline fun <reified VM : ViewModel> Fragment.viewModel() = activityViewModels<VM>(::ViewModelFactory)

fun ViewSwitcher.showFirst() {
    if (getChildAt(0) != currentView) {
        showNext()
    }
}

fun ViewSwitcher.showSecond() {
    if (getChildAt(0) == currentView) {
        showNext()
    }
}
