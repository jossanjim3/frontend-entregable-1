package us.master.entregable1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import us.master.entregable1.R;
import us.master.entregable1.entity.Enlace;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Enlace> itemsEnlace;

    public MenuAdapter(Context context, ArrayList<Enlace> itemsEnlace) {
        this.context = context;
        this.itemsEnlace = itemsEnlace;
    }

    @Override
    public int getCount() {
        return itemsEnlace.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsEnlace.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.enlace, parent, false);
        }

        // get current item to be displayed
        final Enlace currentItem = (Enlace) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItem = (TextView)
                convertView.findViewById(R.id.textViewEnlace);
        ImageView imageView = (ImageView)
                convertView.findViewById(R.id.imageViewEnlace);

        textViewItem.setText(currentItem.getDescripcion());
        imageView.setImageResource(currentItem.getRecursoImageView());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context ,currentItem.getClase());
                context.startActivity(intent);
            }
        });
        // returns the view for the current row
        return convertView;
    }
}
