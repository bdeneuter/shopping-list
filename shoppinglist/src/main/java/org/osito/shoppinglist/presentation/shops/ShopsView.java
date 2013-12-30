package org.osito.shoppinglist.presentation.shops;

import org.osito.shoppinglist.domain.ShopId;

import java.util.List;

public interface ShopsView {

    void setShoppingLists(List<ShopId> shoppingLists);

    void appendShops(List<ShopId> shopIds);
}
