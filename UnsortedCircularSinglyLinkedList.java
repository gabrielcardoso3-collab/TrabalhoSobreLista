package br.com.gomide.list.singly;

import java.util.Objects;

import br.com.gomide.list.ListNode;
import br.com.gomide.list.interfaces.ISinglyLinkedList;
import br.com.gomide.list.interfaces.IUnsortedList;

/**
 * Lista simplesmente encadeada, não ordenada e circular.
 *
 * Característica principal: o próximo do último nó aponta para o primeiro nó,
 * então nenhum nó tem next == null enquanto a lista não estiver vazia.
 */
public class UnsortedCircularSinglyLinkedList<T extends Object>
    implements ISinglyLinkedList<T>, IUnsortedList<T> {

  private ListNode<T> first;
  private ListNode<T> last;
  private ListNode<T> navigationPointer;

  @Override
  public void prepend(T value) {
    ListNode<T> newElement = new ListNode<T>(value);

    if (first == null) {
      newElement.setNext(newElement); // único nó aponta para si mesmo
      this.first = newElement;
      this.last = newElement;
      this.navigationPointer = newElement;
    } else {
      newElement.setNext(this.first);
      this.last.setNext(newElement); // mantém a circularidade
      this.first = newElement;
    }
  }

  @Override
  public void append(T value) {
    ListNode<T> newElement = new ListNode<T>(value);

    if (first == null) {
      newElement.setNext(newElement);
      this.first = newElement;
      this.last = newElement;
      this.navigationPointer = newElement;
    } else {
      newElement.setNext(this.first); // novo último aponta para o primeiro
      this.last.setNext(newElement);
      this.last = newElement;
    }
  }

  @Override
  public T find(T value) {
    if (this.first == null) {
      return null;
    }

    ListNode<T> element = this.first;

    // do-while: como a lista é circular, o fim é quando voltamos ao primeiro
    do {
      T currentValue = element.getValue();

      if (Objects.equals(currentValue, value)) {
        return currentValue;
      }

      element = element.getNext();
    } while (element != this.first);

    return null;
  }

  @Override
  public boolean remove(T value) {
    if (this.first == null) {
      return false;
    }

    ListNode<T> previousNode = this.last;
    ListNode<T> element = this.first;

    do {
      if (Objects.equals(element.getValue(), value)) {
        if (element == previousNode) {
          // era o único elemento da lista
          this.clear();
          return true;
        }

        // o ponteiro de navegação não pode ficar apontando para um nó removido
        if (this.navigationPointer == element) {
          this.navigationPointer = element.getNext();
        }

        previousNode.setNext(element.getNext());

        if (element == this.first) {
          this.first = element.getNext();
        }

        if (element == this.last) {
          this.last = previousNode;
        }

        return true;
      }

      previousNode = element;
      element = element.getNext();
    } while (element != this.first);

    return false;
  }

  @Override
  public void clear() {
    this.first = null;
    this.last = null;
    this.navigationPointer = null;
  }

  @Override
  public String listContent() {
    if (this.first == null) {
      return "null";
    }

    StringBuilder listContent = new StringBuilder();

    ListNode<T> element = this.first;

    do {
      listContent.append(element.getValue());
      listContent.append(" -> ");

      element = element.getNext();
    } while (element != this.first);

    listContent.append("(back to ");
    listContent.append(this.first.getValue());
    listContent.append(")");

    return listContent.toString();
  }

  @Override
  public void clearNavigation() {
    this.navigationPointer = this.first;
  }

  /**
   * Retorna o elemento atual da navegação e avança o ponteiro. Por ser circular,
   * depois do último elemento a navegação volta ao primeiro. Só retorna null se
   * a lista estiver vazia.
   */
  @Override
  public T getNextElement() {
    if (this.first == null || this.navigationPointer == null) {
      return null;
    }

    T value = this.navigationPointer.getValue();
    this.navigationPointer = this.navigationPointer.getNext();
    return value;
  }

}
