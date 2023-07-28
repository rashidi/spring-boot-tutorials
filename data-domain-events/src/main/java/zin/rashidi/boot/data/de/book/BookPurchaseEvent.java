package zin.rashidi.boot.data.de.book;

import org.springframework.context.ApplicationEvent;

/**
 * @author Rashidi Zin
 */
public class BookPurchaseEvent extends ApplicationEvent {

    public BookPurchaseEvent(Book source) {
        super(source);
    }

    @Override
    public Book getSource() {
        return (Book) super.getSource();
    }

}
