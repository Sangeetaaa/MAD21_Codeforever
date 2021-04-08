package com.scitech.codegram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class CodeAdapter extends RecyclerView.Adapter <CodeAdapter.CodeViewHolder> {

    public List<Code> codeList;

    public static class CodeViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView codeName, codeDomain, codeStatus, codeLevel, codeDescription;
        public Context ctx;
        public LinearLayout codeView;

        public CodeViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            mView = itemView;
            codeView = (LinearLayout)mView.findViewById(R.id.codeView);
            codeName = (TextView) mView.findViewById(R.id.codeView_name);
            codeDomain = (TextView) mView.findViewById(R.id.codeView_domain);
            codeStatus = (TextView) mView.findViewById(R.id.codeView_status);
            codeLevel = (TextView) mView.findViewById(R.id.codeView_level);
            codeDescription = (TextView) mView.findViewById(R.id.codeView_description);
        }

        public void setDetails(String name, String domain, String status, String level, String desc) {

            codeName.setText(name);
            codeDomain.setText(domain);
            codeLevel.setText(level);
            codeStatus.setText(status);
            codeDescription.setText(desc);
        }
    }

    public CodeAdapter(List<Code> codeList) {
        this.codeList = codeList;
    }

    @NotNull
    @Override
    public CodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.code_view, parent, false);

        final CodeViewHolder codeHolder = new CodeViewHolder(itemView, parent.getContext());

        return codeHolder;
    }

    @Override
    public void onBindViewHolder(CodeViewHolder holder, int position) {
        Code newCode = codeList.get(position);

        holder.setDetails(newCode.getName(), newCode.getDomain(),
               newCode.getStatus(), newCode.getLevel(), newCode.getDescription());
    }

    @Override
    public int getItemCount() {
        return codeList.size();
    }
}
