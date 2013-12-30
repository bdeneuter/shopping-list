package org.osito.shoppinglist.presentation.shops;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.common.collect.Lists;

import org.osito.shoppinglist.R;
import org.osito.shoppinglist.domain.ShopId;

import java.util.List;

import de.timroes.android.listview.EnhancedListView;

import static com.google.common.collect.Lists.newArrayList;
import static org.osito.shoppinglist.infrastructure.BeanProvider.shopsController;

public class ShopsFragment extends Fragment implements ShopsView, EnhancedListView.OnDismissCallback, AdapterView.OnItemClickListener {

    private ShopsController controller;
    private List<ShopId> shoppingLists = newArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = shopsController(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EnhancedListView view = (EnhancedListView) inflater.inflate(R.layout.fragment_shops, container, false);
        view.setDismissCallback(this);
        view.setRequireTouchBeforeDismiss(false);
        view.enableSwipeToDismiss();
        view.setDrawSelectorOnTop(true);
        view.setAdapter(new ArrayAdapter<ShopId>(
                getActivity(),
                R.layout.item,
                R.id.item,
                Lists.<ShopId>newArrayList()));
        view.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (shoppingLists.isEmpty()) {
            controller.loadShoppingLists(this);
        } else {
            getAdapter().addAll(shoppingLists);
        }
    }

    @Override
    public void setShoppingLists(List<ShopId> shoppingLists) {
        this.shoppingLists = shoppingLists;
        ArrayAdapter adapter = getAdapter();
        adapter.clear();
        adapter.addAll(shoppingLists);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            ShopsDialog dialog = new ShopsDialog();
            dialog.setFragment(this);
            dialog.show(getFragmentManager(), "add_shops");
            return true;
        }
        return false;
    }

    @Override
    public EnhancedListView getView() {
        return (EnhancedListView) super.getView();
    }

    @Override
    public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int position) {
        final ShopId id = shoppingLists.remove(position);
        getAdapter().remove(id);
        return new EnhancedListView.Undoable() {
            @Override
            public void undo() {
                getAdapter().insert(id, position);
                shoppingLists.add(position, id);
            }

            @Override
            public void discard() {
                super.discard();
                controller.removeShop(id);
            }

            @Override
            public String getTitle() {
                return id.getName() + " " + getString(R.string.deleted);
            }
        };
    }

    private ArrayAdapter getAdapter() {
        return (ArrayAdapter) getView().getAdapter();
    }

    public void addShops(String shops) {
        controller.addShops(shops, this);
    }

    @Override
    public void appendShops(List<ShopId> shopIds) {
        this.shoppingLists.addAll(shopIds);
        ArrayAdapter adapter = getAdapter();
        adapter.addAll(shopIds);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        controller.openShoppingList(shoppingLists.get(position), getActivity());
    }
}
