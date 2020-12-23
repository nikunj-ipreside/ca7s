package com.music.ca7s.genericbottomsheet;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;


import com.music.ca7s.R;
import com.music.ca7s.adapter.generic_adapter.GenericAdapter;
import com.music.ca7s.adapter.generic_adapter.SingleSelectionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GenericBottomSheetDialog extends BottomSheetDialogFragment {
    @BindView(R.id.tvHeader)
    TextView tvHeader;
    @BindView(R.id.recycleItemList)
    RecyclerView recycleItemList;
    Unbinder unbinder;
    GenericAdapter<GenericBottomModel, BottomSheetViewHolder> genericAdapter;
    private GenericBottomAdapter adapter;
    private BottomSheetBehavior<View> mBehavior;
    private RecyclerItemClick callback;
    private List<GenericBottomModel> datalist;
    private String header = "";
    //    private boolean multiSelection;
    private List<GenericBottomModel> selectedItem = new ArrayList<>();

    public void setCallback(RecyclerItemClick callback) {
        this.callback = callback;
    }

    public void setDatalist(List<GenericBottomModel> datalist) {
        this.datalist = datalist;
    }

    public void setHeader(String header) {
        this.header = header;
    }

//    public void setMultiSelection(boolean multiSelection) {
//        this.multiSelection = multiSelection;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomdialog_generic_item_picker, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvHeader.setText(header);

        /*if (multiSelection) {
            genericAdapter = new MultipleSelectionAdapter<GenericBottomModel, CheckViewHolder>(
                    R.layout.row_generic_bottomsheet_item, CheckViewHolder.class, datalist) {
                @Override
                public void setViewHolderData(CheckViewHolder viewHolderData, final GenericBottomModel data, int position) {
                    DebugLog.e("Size" + position);
                    viewHolderData.tvItemText.setText(data.getItemText());
                    viewHolderData.lrMain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            callback.onItemClick(data);
                        }
                    });
                }
            };
        } else {
            genericAdapter = new GenericAdapter<GenericBottomModel, CheckViewHolder>(
                    R.layout.row_generic_bottomsheet_item, CheckViewHolder.class, datalist) {
                @Override
                public void setViewHolderData(CheckViewHolder viewHolderData, final GenericBottomModel data, int position) {
                    DebugLog.e("Size" + position);
                    viewHolderData.tvItemText.setText(data.getItemText());
                    viewHolderData.lrMain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            callback.onItemClick(data);
                        }
                    });
                }
            };
        }
*/


/*        if (multiSelection) {
            genericAdapter = new MultipleSelectionAdapter<GenericBottomModel, BottomSheetViewHolder>(R.layout.row_multiselection_generic_bottomsheet_item, BottomSheetViewHolder.class, datalist) {
                @Override
                public void setViewHolderData(BottomSheetViewHolder viewHolderData, final GenericBottomModel data, int position) {
                    if (selectedItem.contains(data)) {
                        selectedItem.remove(data);
                    } else {
                        selectedItem.add(data);
                    }
                    viewHolderData.tvItemText.setText(data.getItemText());
                    viewHolderData.lrMain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            callback.onItemMultipleSelected(selectedItem);
                        }
                    });
                }
            };
        } else {*/
        genericAdapter = new SingleSelectionAdapter<GenericBottomModel, BottomSheetViewHolder>(R.layout.row_generic_bottomsheet_item, BottomSheetViewHolder.class, datalist) {
            @Override
            public Filter getFilter() {
                return null;
            }

            @Override
            public void setViewHolderData(BottomSheetViewHolder viewHolderData, final GenericBottomModel data, int position) {
                viewHolderData.tvItemText.setText(data.getItemText());
                viewHolderData.lrMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        callback.onItemClick(data);
                    }
                });
            }

            @Override
            public void loadMore() {

            }
        };
//        }


        recycleItemList.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleItemList.setAdapter(genericAdapter);
    }

    @Override
    public void onDestroyView() {
       /* if (multiSelection)
            callback.onItemMultipleSelected(selectedItem);*/

        super.onDestroyView();
        unbinder.unbind();
    }

    public interface RecyclerItemClick {
        void onItemClick(GenericBottomModel genericBottomModel);
//        void onItemMultipleSelected(FollowersList<GenericBottomModel> genericBottomModel);
    }


}
