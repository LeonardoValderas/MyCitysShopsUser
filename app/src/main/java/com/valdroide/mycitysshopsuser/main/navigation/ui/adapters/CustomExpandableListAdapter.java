package com.valdroide.mycitysshopsuser.main.navigation.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Category> mExpandableListCategories;
    private Map<String, List<SubCategory>> mExpandableListSubcategories;
    private List<String> mExpandableListTitle;
    private LayoutInflater mLayoutInflater;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       Map<String, List<SubCategory>> expandableListDetail) {
        mContext = context;
        mExpandableListTitle = expandableListTitle;
        mExpandableListSubcategories = expandableListDetail;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mExpandableListSubcategories.get(mExpandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String categoty = (String) getGroup(listPosition);
        int isUpdate = getUpdateCategory(categoty);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textViewCategory = (TextView) convertView
                .findViewById(R.id.listTitle);
        textViewCategory.setTypeface(null, Typeface.BOLD);
        textViewCategory.setText(categoty);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewIcon);
        if (isUpdate == 1)
            imageView.setVisibility(View.VISIBLE);

        try {
            textViewCategory.setTypeface(Utils.setFontGoodDogTextView(mContext));
        } finally {

        }

        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final SubCategory expandedListText = (SubCategory) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, null);
        }
        TextView textViewSubCategory = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        textViewSubCategory.setText(expandedListText.getSUBCATEGORY());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewIcon);
        if (expandedListText.getIS_UPDATE() == 1)
            imageView.setVisibility(View.VISIBLE);

        try {
            textViewSubCategory.setTypeface(Utils.setFontGoodDogTextView((mContext)));
        } finally {

        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return mExpandableListSubcategories.get(mExpandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {

        return mExpandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return mExpandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void setList(List<Category> expandableListCategories, List<String> expandableListTitle, Map<String, List<SubCategory>> expandableListDetail) {
        mExpandableListCategories = expandableListCategories;
        mExpandableListTitle = expandableListTitle;
        mExpandableListSubcategories = expandableListDetail;
        notifyDataSetChanged();
    }

    public int getUpdateCategory(String name) {
        for (int i = 0; i < mExpandableListCategories.size(); i++) {
            if (mExpandableListCategories.get(i).getCATEGORY().equalsIgnoreCase(name)) {
                return mExpandableListCategories.get(i).getIS_UPDATE();
            }
        }
        return 0;
    }
}