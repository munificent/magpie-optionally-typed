﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Magpie.Compilation
{
    /// <summary>
    /// Contains the context in which expression or declaration binding occurs.
    /// For example, the current name context for looking up names, the set
    /// of type arguments, etc.
    /// </summary>
    public class BindingContext
    {
        public Compiler Compiler { get; private set; }
        public NameSearchSpace SearchSpace { get; private set; }
        public IDictionary<string, IBoundDecl> TypeArguments { get; private set; }

        public BindingContext(Compiler compiler, NameSearchSpace searchSpace)
            : this(compiler, searchSpace, new Dictionary<string, IBoundDecl>())
        {
        }

        public BindingContext(Compiler compiler, NameSearchSpace searchSpace, IDictionary<string, IBoundDecl> typeArguments)
        {
            Compiler = compiler;
            SearchSpace = searchSpace;
            TypeArguments = typeArguments;
        }
    }
}
