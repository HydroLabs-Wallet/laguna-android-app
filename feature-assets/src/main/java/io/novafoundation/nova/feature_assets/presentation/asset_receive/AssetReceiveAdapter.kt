package io.novafoundation.nova.feature_assets.presentation.asset_receive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.ImageLoader
import coil.load
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.receive.model.TokenReceiver

class AssetReceiveAdapter(
    context: Context,
    var dataSource: List<TokenReceiver>,
    val imageLoader: ImageLoader
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinneritem_receive, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        val asset = dataSource[position]
        vh.label.text =asset.addressModel.name
        vh.img.load(asset.addressModel.image, imageLoader)
//        vh.imNotNative.isVisible = asset.token.configuration.chainName !=  asset.token.configuration.name
//        if (asset.token.configuration.name != asset.token.configuration.chainName) {
//            vh.imNotNative.isVisible = true
//            vh.imNotNative.load(asset.token.configuration.chainIcon, imageLoader)
//        } else {
//            vh.imNotNative.isVisible = false
//        }
        return view
    }

    override fun getItem(position: Int): TokenReceiver {
        return dataSource[position]
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val img: ImageView
        val imNotNative: ImageView

        init {
            label = row?.findViewById(R.id.tvTitle) as TextView
            img = row.findViewById(R.id.imgIcon) as ImageView
            imNotNative = row.findViewById(R.id.imNotNative) as ImageView
        }
    }

}
