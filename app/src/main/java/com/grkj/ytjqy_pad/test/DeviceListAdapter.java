package com.grkj.ytjqy_pad.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dothantech.printer.IDzPrinter;
import com.grkj.ytjqy_pad.R;

import java.util.List;

/**
 * 用于填充打印机列表的Adapter
 */
public class DeviceListAdapter extends BaseAdapter {
    private TextView tv_name = null;
    private TextView tv_mac = null;

    private Context context;
    private List<IDzPrinter.PrinterAddress> pairedPrinters;

    public DeviceListAdapter(Context context, List<IDzPrinter.PrinterAddress> pairedPrinters) {
        this.context = context;
        this.pairedPrinters = pairedPrinters;
    }

    @Override
    public int getCount() {
        return pairedPrinters.size();
    }

    @Override
    public Object getItem(int position) {
        return pairedPrinters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.printer_item, null);
        }
        tv_name = convertView.findViewById(R.id.tv_device_name);
        tv_mac = convertView.findViewById(R.id.tv_macaddress);

        if (pairedPrinters != null && pairedPrinters.size() > position) {
            IDzPrinter.PrinterAddress printer = pairedPrinters.get(position);
            tv_name.setText(printer.shownName);
            tv_mac.setText(printer.macAddress);
        }

        return convertView;
    }
}
