package com.example.muhamed.kurani;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RecycleViewAyahListingAdapter extends RecyclerView.Adapter<RecycleViewAyahListingAdapter.AyahViewHolder>{

    private ArrayList<AyahItem> mAyahItems;
    Context thisContext;
    FragmentManager fragManager;
    int mSurahNumber;
    public RecycleViewAyahListingAdapter(Context context,ArrayList<AyahItem> ayahItems,FragmentManager fm,int surahNumber)
    {
        thisContext = context;
        mAyahItems = ayahItems;
        fragManager = fm;
        mSurahNumber = surahNumber;
    }

    @NonNull
    @Override
    public AyahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_ayah_list_item,parent,false);
        AyahViewHolder viewHolder = new AyahViewHolder(view);
        return  viewHolder;
    }


    //Kjo metode shfrytezohet per te shfaqur ikonat e context menus qe shfaqet kur bejme long
    //click ne ajetin perkates
    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AyahViewHolder holder, int position) {
        final AyahItem currentItem = mAyahItems.get(position);
        holder.mNumberOfAyah.setText(currentItem.getNumberOfAyah());
        holder.mAyah_Arab.setText(currentItem.getAyah_Arab());
        holder.mAyah_Alb.setText(currentItem.getAyah_Alb());
        holder.setOnItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                 PopupMenu popupmenu = new PopupMenu(thisContext,v);
                 MenuInflater inflater = popupmenu.getMenuInflater();
                 inflater.inflate(R.menu.ayah_longclick_context_menu, popupmenu.getMenu());
                 setForceShowIcon(popupmenu);
                 popupmenu.show();
                 popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         int itemId = item.getItemId();

                         StringBuilder sb = new StringBuilder();
                         sb.append(Integer.toString(mSurahNumber)+ ":" +currentItem.getNumberOfAyah());
                         sb.append(System.getProperty("line.separator"));
                         sb.append(currentItem.getAyah_Arab());
                         sb.append(currentItem.getAyah_Alb());

                         switch(itemId){
                             case R.id.copy:
                             {
                                 ClipboardManager clipboard = (ClipboardManager)thisContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                 ClipData clip = ClipData.newPlainText("Ajeti i kopjuar",sb.toString());
                                 clipboard.setPrimaryClip(clip);
                                 Toast.makeText(thisContext,"Ajeti është kopjuar me sukses.",Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             case R.id.bookmark:
                             {
                                 if (mSurahNumber != -1) {
                                     boolean saved = Helper.SaveBookmarkedAyaToInternalStorage(Constants.filenameForBookMark, thisContext, Integer.toString(mSurahNumber), currentItem.getNumberOfAyah());
                                     if (saved)
                                     {
                                         Toast.makeText(thisContext,"Ajeti ku keni mbetur është ruajtur me sukses.",Toast.LENGTH_SHORT).show();
                                     }
                                 }
                                 break;
                             }
                             case R.id.fbshare:
                             {
                                 String shareBody = sb.toString();
                                 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                 sharingIntent.setType("text/plain");
                                 sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kurani");
                                 sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                 thisContext.startActivity(Intent.createChooser(sharingIntent, "Share using..."));
                                 break;
                             }
                             case R.id.saveayah: {
                                 //File dir = thisContext.getFilesDir();
                                 //File file = new File(dir, Constants.filenameForAyahSavings);
                                 //file.delete();
                                  if (mSurahNumber != -1) {
                                     boolean saved = Helper.SaveToSavingAyasToInternalStorage(Constants.filenameForAyahSavings, thisContext, Integer.toString(mSurahNumber), currentItem.getNumberOfAyah());
                                     if (!saved) {
                                         Toast.makeText(thisContext,"Ajeti është ruajtur me sukses.",Toast.LENGTH_SHORT).show();
                                     }
                                     else {
                                         Toast.makeText(thisContext,"Ky ajet aktualisht ekziston te ruajtjet !.",Toast.LENGTH_SHORT).show();
                                     }
                                 }
                                 break;
                                 //String test = Helper.ReadXmlForTest(thisContext,"savingsAyas.xml");
                             }
                         }
                         return false;
                     }
                 });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAyahItems.size();
    }


    public static class AyahViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        public TextView mNumberOfAyah;
        public TextView mAyah_Arab;
        public TextView mAyah_Alb;
        ItemLongClickListener itemLongClickListener;

        public AyahViewHolder(View itemView) {
            super(itemView);
            mNumberOfAyah = itemView.findViewById(R.id.tv_ayah_number);
            mAyah_Arab = itemView.findViewById(R.id.tv_ayah_arab);
            mAyah_Alb = itemView.findViewById(R.id.tv_ayah_albanian);
            itemView.setOnLongClickListener(this);
        }

        public void setOnItemLongClickListener(ItemLongClickListener ic)
        {
           this.itemLongClickListener = ic;
        }

        @Override
        public boolean onLongClick(View v) {
            this.itemLongClickListener.onItemLongClick(v,getLayoutPosition());
            return false;
        }

    }

    public interface ItemLongClickListener{
        void onItemLongClick(View v, int position);
    }
}
