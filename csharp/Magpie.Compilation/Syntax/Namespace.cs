﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Magpie.Compilation
{
    public class Namespace
    {
        public string Name;

        public readonly List<Namespace> Namespaces = new List<Namespace>();
        public readonly List<Function> Functions = new List<Function>();
        public readonly List<Struct> Structs = new List<Struct>();
        public readonly List<Union> Unions = new List<Union>();
        public readonly List<GenericFunction> GenericFunctions = new List<GenericFunction>();
        public readonly List<GenericStruct> GenericStructs = new List<GenericStruct>();
        public readonly List<GenericUnion> GenericUnions = new List<GenericUnion>();

        //### bob: using object here is gross
        public Namespace(string name, IEnumerable<object> contents)
        {
            Name = name;

            foreach (object obj in contents)
            {
                if (obj is Namespace) Namespaces.Add((Namespace)obj);
                else if (obj is Function) Functions.Add((Function)obj);
                else if (obj is Struct) Structs.Add((Struct)obj);
                else if (obj is Union) Unions.Add((Union)obj);
                else if (obj is GenericFunction) GenericFunctions.Add((GenericFunction)obj);
                else if (obj is GenericStruct) GenericStructs.Add((GenericStruct)obj);
                else if (obj is GenericUnion) GenericUnions.Add((GenericUnion)obj);
            }
        }

        public string ContentsToString()
        {
            var builder = new StringBuilder();

            foreach (var child in Namespaces)
            {
                builder.AppendLine("namespace " + child.Name);
                builder.AppendLine(child.ContentsToString());
                builder.AppendLine("end");
            }

            foreach (var function in Functions)
            {
                builder.AppendLine(function.ToString());
            }

            foreach (var structure in Structs)
            {
                builder.AppendLine(structure.ToString());
            }

            foreach (var union in Unions)
            {
                builder.AppendLine(union.ToString());
            }

            return builder.ToString();
        }

        public override string ToString()
        {
            return Name + ":";
        }
    }
}