package classes;

import play.mvc.Result;

public interface LibraryManager {

    public Result addBook();
    public Result addDVD();
    public Result deleteItem(int isbn);
    public Result listAll();
    public Result borrowItem();
    public Result returnItem() throws Exception;
    public Result report();
    public Result addReader();
    public Result find(int isbn);
    public Result reserve();
    public Result search(String title);
}
