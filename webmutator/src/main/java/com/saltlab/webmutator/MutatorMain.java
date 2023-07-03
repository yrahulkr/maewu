package com.saltlab.webmutator;

import com.saltlab.webmutator.plugins.DefaultNodePicker;

/**
 * Hello world!
 *
 */
public class MutatorMain 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        WebMutator mutator = new WebMutator("/Users/rahulkrishna/git/art_fork/art/crawljax/plugins/testcasegenerator-plugin/src/test/resources/petclinic_HYBRID_0.0_5mins/localhost/crawl0", MutationMode.DOM,
        		new DefaultNodePicker());
        mutator.mutateState("index");
    }
}
