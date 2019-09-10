package io.github.viscent.mtia.ch12;

public class LockElisionExample {

    public static void main(String[] args) {
        ProductInfo pi = new ProductInfo();
        pi.setProductID("P0000001");
        pi.setCategoryID("C0010");
        pi.setInventory(100);
        pi.setRank(18);
        System.out.println(toJSON(pi));

    }

    public static String toJSON(ProductInfo productInfo) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("{\"productID\":\"").append(productInfo.productID);
        sbf.append("\",\"categoryID\":\"").append(productInfo.categoryID);
        sbf.append("\",\"rank\":").append(productInfo.rank);
        sbf.append(",\"inventory\":").append(productInfo.inventory);
        sbf.append('}');

        return sbf.toString();
    }

    static class ProductInfo {
        private String productID;
        private String categoryID;
        private int rank;
        private int inventory;

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public String getCategoryID() {
            return categoryID;
        }

        public void setCategoryID(String categoryID) {
            this.categoryID = categoryID;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

    }

}
