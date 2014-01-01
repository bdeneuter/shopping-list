package org.osito.shoppinglist.presentation.shoppinglist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.osito.shoppinglist.R;
import org.osito.shoppinglist.domain.Item;

import java.util.List;

import de.timroes.android.listview.EnhancedListView;

import static com.google.common.collect.Lists.newArrayList;
import static org.osito.shoppinglist.infrastructure.BeanProvider.shoppingListController;

public class ItemsFragment extends Fragment implements ItemsView, EnhancedListView.OnDismissCallback {

    private ItemsController controller;
    private List<Item> items = newArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        controller = shoppingListController(getActivity());
        controller.init(getActivity().getIntent().getLongExtra("shop_id", -1), this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EnhancedListView view = (EnhancedListView) inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        view.setDismissCallback(this);
        view.setRequireTouchBeforeDismiss(false);
        view.enableSwipeToDismiss();
        view.setDrawSelectorOnTop(true);
        view.setAdapter(new ArrayAdapter<Item>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                newArrayList(items)));
        return view;
    }

    @Override
    public void setItems(List<Item> items) {
        this.items = items;
        ArrayAdapter adapter = getAdapter();
        adapter.clear();
        adapter.addAll(items);
    }

    @Override
    public void appendItems(List<Item> items) {
        this.items.addAll(items);
        getAdapter().addAll(items);
    }

    @Override
    public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int position) {
        final Item item = items.remove(position);
        getAdapter().remove(item);
        return new EnhancedListView.Undoable() {
            @Override
            public void undo() {
                getAdapter().insert(item, position);
                items.add(position, item);
            }

            @Override
            public void discard() {
                super.discard();
                controller.removeItem(item);
            }

            @Override
            public String getTitle() {
                return item.getName() + " " + getString(R.string.deleted);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            ItemsDialog dialog = new ItemsDialog();
            dialog.setFragment(this);
            dialog.show(getFragmentManager(), "add_items");
            return true;
        }
        return false;
    }

    public void addItems(String items) {
        controller.addItems(items, this);
    }

    @Override
    public EnhancedListView getView() {
        return (EnhancedListView) super.getView();
    }

    private ArrayAdapter getAdapter() {
        return (ArrayAdapter) getView().getAdapter();
    }
}
