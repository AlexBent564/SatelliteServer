
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
        // if specified parameter is 1 return second value of Fibonacci series
        if( ( Integer )parameters == 1 )
        {
            return 1;
        }
        // else specified parameter is greater than 1
        else 
        {
            return go( (( Integer )parameters - 1) + ( ( Integer )parameters - 2) );
        }
    }
}
