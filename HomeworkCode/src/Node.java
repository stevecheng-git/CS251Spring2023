public class Node {
    public Node(String val) {
        this.val = val;
    }

    public String val;
    public Node prev, next, child;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
        prev.next = this;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
        next.prev = this;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Node{" +
                "val='" + val + '\'' +
                '}';
    }

    static class SpecialLinkedList {
        public Node steamRoll(Node head) {
            System.out.println(roller(head));
            return head;
        }

        private Node roller(Node head) {
            if (head == null) {
                return null;
            }
            Node pointer = head;
            Node prev = head;

            while (pointer != null) {
                if (pointer.child != null) {
                    Node childTail = roller(pointer.child);
                    Node next = pointer.next;
                    pointer.child.prev = pointer;
                    pointer.next = pointer.child;
                    pointer.child = null;
                    childTail.next = next;
                    if (next != null) {
                        next.prev = childTail;
                    }
                    pointer = childTail;
                }
                prev = pointer;
                pointer = pointer.next;
            }
            return prev;
        }
    }

    public static void main(String[] args) {

        Node a = new Node("A"),
                b = new Node("B"),
                c = new Node("C"),
                d = new Node("D"),
                e = new Node("E"),
                f = new Node("F"),
                g = new Node("G"),
                h = new Node("H"),
                i = new Node("I"),
                j = new Node("J"),
                k = new Node("K"),
                l = new Node("L"),
                m = new Node("M"),
                n = new Node("N"),
                o = new Node("O"),
                p = new Node("P"),
                q = new Node("Q"),
                r = new Node("R"),
                s = new Node("S"),
                t = new Node("T"),
                u = new Node("U"),
                v = new Node("V"),
                w = new Node("W"),
                x = new Node("X"),
                y = new Node("Y"),
                z = new Node("Z");

        a.setNext(b);
        b.setNext(c);
        c.setNext(d);
        d.setNext(e);
        e.setNext(f);

        a.setChild(h);
        h.setChild(l);
        h.setNext(j);
        j.setNext(k);
        l.setNext(m);
        m.setNext(n);

        d.setChild(o);
        o.setNext(p);
        p.setNext(g);
        p.setChild(x);
        x.setNext(y);
        y.setNext(z);
        x.setChild(w);
        w.setNext(u);
        u.setNext(v);

        SpecialLinkedList specialLinkedList = new SpecialLinkedList();
//        System.out.println(specialLinkedList.roller(a));
//        System.out.println(specialLinkedList.steamRoll(a));
        System.out.println(specialLinkedList.steamRoll(h));
//    System.out.println(specialLinkedList.roller(b));
//    System.out.println(specialLinkedList.roller(c));
//    System.out.println(specialLinkedList.roller(d));
//    System.out.println(specialLinkedList.roller(e));
//    System.out.println(specialLinkedList.roller(f));
    }
}

