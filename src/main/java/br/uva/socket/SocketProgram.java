/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.uva.socket;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author 19485701730
 */
public class SocketProgram {

    public interface Function3<A, B, R> {
        R apply(A a, B b);
    }
    
    public enum Operation {
        SUM(1, "Somar", Double::sum, a -> false, b -> false),
        SUBTRACT(2, "Subtrair", (a, b) -> a - b, a -> false, b -> false),
        DIVIDE(3, "Dividir", (a, b) -> a / b, a -> false, b -> b == 0),
        MULTIPLY(4, "Multiplicar", (a, b) -> a * b, a -> false, b -> false);
        
        public static Operation findById(byte id) {
            return Stream.of(values()).filter(op -> op.id == id).findFirst().orElse(null);
        }
        
        public final byte id;
        public final String displayName;
        private final Function3<Double, Double, Double> makeFunction;
        private final Predicate<Double> invalidateA, invalidateB;
        
        Operation(int id, String name, Function3<Double, Double, Double> makeFunction, Predicate<Double> invalidateA, Predicate<Double> invalidateB) {
            this.id = (byte) id;
            this.displayName = name;
            this.makeFunction = makeFunction;
            this.invalidateA = invalidateA;
            this.invalidateB = invalidateB;
        }
        
        public double make(double a, double b) {
            return makeFunction.apply(a, b);
        }
        
        public boolean invalidate(double a, double b) {
            return invalidateA.test(a) || invalidateB.test(b);
        }
    }
}