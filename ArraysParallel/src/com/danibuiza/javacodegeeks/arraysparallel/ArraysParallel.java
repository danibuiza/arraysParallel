package com.danibuiza.javacodegeeks.arraysparallel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntToDoubleFunction;

public class ArraysParallel
{

    public static void main( String[] args )
    {
        sorting();

        functions();

        prefix();

        prefixSmallExample();

        streams();

    }

    /**
     * this methods shows exactly what the parallelPrefix method does
     */
    private static void prefixSmallExample()
    {
        double[] array = { 1.0, 2.0, 3.0 };

        for( int i = 0; i < 3; i++ )
        {
            System.out.println( "element at pos " + i + " previously " + array[i] );
        }

        Arrays.parallelPrefix( array, ( x, y ) -> {
            if( x % 2 != 0 )
                return y * 2;
            return y;
        } );

        for( int i = 0; i < 3; i++ )
        {
            System.out.println( "element at pos " + i + " after " + array[i] );
        }
    }

    private static void streams()
    {
        double[] array = new double[10000];
        for( int i = 0; i < 10000; i++ )
        {
            array[i] = Math.random() % 100;
            System.out.println( "element at pos " + i + " previously " + array[i] );
        }

        // DoubleStream streamFromArray = Arrays.stream( array );

        // all streams functionalities are available
        Arrays.stream( array ).filter( x -> x > 10 ).forEach( System.out::println );

        // also using parallel streams
        Arrays.stream( array ).filter( x -> x > 10 ).parallel().forEach( System.out::println );

    }

    private static void prefix()
    {
        /* populate the array */
        double[] array = new double[10];
        for( int i = 0; i < 10; i++ )
        {
            array[i] = Math.random() % 100;
            System.out.println( "element at pos " + i + " previously " + array[i] );
        }

        DoubleBinaryOperator binaryOperator = new DoubleBinaryOperator()
        {

            @Override
            public double applyAsDouble( double x, double y )
            {
                return x * y;
            }
        };

        /* we can use binary operators as prefix in parallel */
        Arrays.parallelPrefix( array, binaryOperator );

        for( int i = 0; i < 10; i++ )
        {
            System.out.println( "element at pos " + i + " after prefix operator applied " + array[i] );
        }

        /* also using lambdas */
        Arrays.parallelPrefix( array, ( x, y ) -> x + y );

        for( int i = 0; i < 10; i++ )
        {
            System.out.println( "element at pos " + i + " after prefix operator applied " + array[i] );
        }

        /* it is possible to pass start and end index to perform the sorting only in this range */
        int startIndex = 3;
        int endIndex = 7;
        Arrays.parallelPrefix( array, startIndex, endIndex, ( x, y ) -> x * y );

        for( int i = 0; i < 10; i++ )
        {
            System.out.println( "element at pos " + i + " after prefix operator applied " + array[i] );
        }

    }

    private static void functions()
    {
        /* populate the array */
        double[] array = new double[10000];
        for( int i = 0; i < 10000; i++ )
        {
            array[i] = Math.random() % 100;
            System.out.println( "element at pos " + i + " previously " + array[i] );
        }

        IntToDoubleFunction functionToUse = new IntToDoubleFunction()
        {

            @Override
            public double applyAsDouble( int x )
            {
                return x * 100;
            }
        };
        /* it is possible to apply functions to all elements of the array */
        Arrays.parallelSetAll( array, functionToUse );

        for( int i = 0; i < 10000; i++ )
        {
            System.out.println( "element at pos " + i + " after function applied " + array[i] );
        }

        // using lambdas as well (double function ;-))
        Arrays.parallelSetAll( array, x -> x * 100 );

        for( int i = 0; i < 10000; i++ )
        {
            System.out.println( "element at pos " + i + " after function applied " + array[i] );
        }

    }

    private static void sorting()
    {
        /* populate the array */
        double[] array = new double[1000000];
        for( int i = 0; i < 10000; i++ )
        {
            array[i] = Math.random() % 100;
        }

        long start = System.nanoTime();
        Arrays.parallelSort( array );
        long end = System.nanoTime();

        System.out.println( ( end - start ) + " ns. parallelSort" );

        start = System.nanoTime();
        Arrays.sort( array );
        end = System.nanoTime();

        // actually is quicker than the other, probably depends on the VM and other parameters
        System.out.println( ( end - start ) + " ns. sort" );

        String[] arrayStr = new String[10000];
        for( int i = 0; i < 10000; i++ )
        {
            arrayStr[i] = Double.toString( Math.random() % 100 );
        }

        Comparator<String> comparator = new Comparator<String>()
        {

            @Override
            public int compare( String str1, String str2 )
            {
                return str1.compareTo( str2 );
            }
        };

        Arrays.parallelSort( arrayStr, comparator );

        // as Lambda expression
        Arrays.parallelSort( arrayStr, ( x, y ) -> x.compareTo( y ) );

        /* it is possible to pass start and end index to perform the sorting only in this range */
        int startIndex = 100;
        int endIndex = 150;
        Arrays.parallelSort( arrayStr, startIndex, endIndex, comparator );

    }
}
