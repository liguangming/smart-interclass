/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.incito.classroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.vo.LoginRes2Vo;

/**
 * © 2012 amsoft.cn
 * 名称：CarouselViewAdapter.java
 * 描述：自定义View适配的Carousel
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-8-22 下午4:05:09
 */
public class GroupNumAdapter extends BaseAdapter {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The m views.
     */
    private List<LoginRes2Vo> datas;

    /**
     * The m reflected.
     */
    private boolean mReflected = true;

    LayoutInflater mInflater;

    /**
     * Instantiates a new carousel view adapter.
     *
     * @param context the c
     * @param datas   the views
     */
    public GroupNumAdapter(Context context, List<LoginRes2Vo> datas) {
        this.mContext = context;
        this.datas = datas;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 描述：TODO.
     *
     * @return the count
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return datas.size();
    }

    /**
     * 描述：TODO.
     *
     * @param position the position
     * @return the item
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    /**
     * 描述：TODO.
     *
     * @param position the position
     * @return the item id
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 描述：TODO.
     *
     * @param position    the position
     * @param convertView the convert view
     * @param parent      the parent
     * @return the view
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_group_mem, parent, false);
            holder = new ViewHolder();
            holder.tv_num_name = (TextView) convertView.findViewById(R.id.tv_num_name);
            holder.rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (datas.get(position).getIslogin() == "0") {
            holder.rlayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_logged_user_m));
        } else {
            holder.rlayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_not_logged_user_m));
        }
        holder.tv_num_name.setText(datas.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        public TextView tv_num_name;
        public RelativeLayout rlayout;
    }

    public void setDatas(List<LoginRes2Vo> datas) {
        this.datas = datas;
    }
}
