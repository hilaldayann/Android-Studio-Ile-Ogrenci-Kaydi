package com.gop.ogrencikayitprojesi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HD_OgrenciAdapter extends RecyclerView.Adapter<HD_OgrenciAdapter.ViewHolder> {

    private ArrayList<HD_OgrenciBilgi> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView hd_ogrNo;
        private final TextView hd_ogrAdi;
        private final TextView hd_ogrSoyadi;
        private final TextView hd_ePosta;
        private final ImageView hd_foto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hd_ogrNo = (TextView) itemView.findViewById(R.id.hd_txt_anaNo);
            hd_ogrAdi = (TextView) itemView.findViewById(R.id.hd_txt_anaisim);
            hd_ogrSoyadi = (TextView) itemView.findViewById(R.id.hd_txt_anasoyisim);
            hd_ePosta = (TextView) itemView.findViewById(R.id.hd_txt_anaMail);
            hd_foto = itemView.findViewById(R.id.hd_iv_anaFoto);
        }
    }

    public HD_OgrenciAdapter(ArrayList<HD_OgrenciBilgi> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hd_analiste, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.hd_ogrNo.setText(localDataSet.get(position).hd_ogrNo);
        viewHolder.hd_ogrAdi.setText(localDataSet.get(position).hd_ogrAdi);
        viewHolder.hd_ogrSoyadi.setText(localDataSet.get(position).hd_ogrSoyadi);
        viewHolder.hd_ePosta.setText(localDataSet.get(position).hd_ePosta);
        String url = localDataSet.get(position).url;
        Picasso.get().load(url).into(viewHolder.hd_foto);

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
