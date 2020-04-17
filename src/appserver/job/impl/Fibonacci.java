
package appserver.job.impl;

import appserver.job.Tool;

/**
 * Class [Fibonacci] Performs the Fibonacci function when called and returns
 * the number in the Fibonacci series according to the parameter
 * 
 */
public class Fibonacci implements Tool {
    
    @Override
    public Object go( Object parameters ) {
        
        // parameter will never be 0 but this block of code is necessary for the recursion
        if( ( Integer )parameters == 0 )
        {
            return 0;
        }
        // if parameter is 1, return second value of Fibonacci series
        else if( ( Integer )parameters == 1 )
        {
            return 1;
        }
        // else parameter is greater than 1, return corresponding value of Fibonacci series
        else
        {
            return ( Integer )go( ( Integer )parameters - 1 ) + ( Integer )go( ( Integer )parameters - 2 );
        }
    }
}
