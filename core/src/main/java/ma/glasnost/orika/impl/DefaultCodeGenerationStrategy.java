/*
 * Orika - simpler, better and faster Java bean mapping
 *
 * Copyright (C) 2011-2013 Orika authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ma.glasnost.orika.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.generator.AggregateSpecification;
import ma.glasnost.orika.impl.generator.CodeGenerationStrategy;
import ma.glasnost.orika.impl.generator.Specification;
import ma.glasnost.orika.impl.generator.specification.*;


/**
 * @author matt.deboer@gmail.com
 * 
 */
public class DefaultCodeGenerationStrategy implements CodeGenerationStrategy {
    
    private final List<Specification> specifications;
    private final List<AggregateSpecification> aggregateSpecifications;
    
    public DefaultCodeGenerationStrategy() {
        
        this.specifications = new CopyOnWriteArrayList<Specification>(
                Arrays.asList(
                        new ConvertArrayOrCollectionToArray(),
                        new ConvertArrayOrCollectionToCollection(),
                        new MapPropertyConvert(),
                        new Convert(), 
                        new CopyByReference(), 
                        new ApplyRegisteredMapper(),
                        new EnumToEnum(), 
                        new StringToEnum(), 
                        new UnmappableEnum(), 
                        new ArrayOrCollectionToArray(),
                        new ArrayOrCollectionToCollection(), 
                        new MapToMap(), 
                        new MapToArray(), 
                        new MapToCollection(), 
                        new ArrayOrCollectionToMap(),
                        new StringToStringConvertible(), 
                        new AnyTypeToString(), 
                        new MultiOccurrenceElementToObject(),
                        new ObjectToMultiOccurrenceElement(), 
                        new PrimitiveAndObject(), 
                        new ObjectToObject()));
        
        this.aggregateSpecifications = new CopyOnWriteArrayList<AggregateSpecification>(
                Arrays.asList(new MultiOccurrenceToMultiOccurrence()));
        
    }
    
    public void setMapperFactory(MapperFactory mapperFactory) {
        for (Specification spec : this.specifications) {
            spec.setMapperFactory(mapperFactory);
        }
        for (AggregateSpecification spec : this.aggregateSpecifications) {
            spec.setMapperFactory(mapperFactory);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * ma.glasnost.orika.impl.generator.CodeGenerationStrategy#addSpecification
     * (ma.glasnost.orika.impl.generator.Specification,
     * ma.glasnost.orika.impl.generator.CodeGenerationStrategy.Position,
     * ma.glasnost.orika.impl.generator.Specification)
     */
    public void addSpecification(Specification spec, Position relativePosition, Class<Specification> relativeSpec) {
        addSpec(this.specifications, spec, relativePosition, relativeSpec);
    }
    
    protected static <T> void addSpec(List<T> specifications, T spec, Position relativePosition, Class<T> relativeSpec) {
        
        if (relativePosition == null || relativePosition == Position.LAST) {
            specifications.add(spec);
        } else if (relativePosition == Position.FIRST) {
            specifications.add(0, spec);
        } else {
            for (int i = 0, len = specifications.size(); i < len; ++i) {
                T s = specifications.get(i);
                if (s.getClass().equals(relativeSpec)) {
                    switch (relativePosition) {
                    case IN_PLACE_OF:
                        specifications.remove(i);
                        break;
                    case BEFORE:
                        break;
                    case AFTER:
                        ++i;
                        break;
                    case LAST:
                        break;
                    case FIRST:
                    default:
                        break;
                    }
                    specifications.add(i, spec);
                    break;
                }
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * ma.glasnost.orika.impl.generator.CodeGenerationStrategy#getSpecifications
     * ()
     */
    public List<Specification> getSpecifications() {
        return specifications;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ma.glasnost.orika.impl.generator.CodeGenerationStrategy#
     * addAggregateSpecification
     * (ma.glasnost.orika.impl.generator.AggregateSpecification,
     * ma.glasnost.orika.impl.generator.CodeGenerationStrategy.Position,
     * ma.glasnost.orika.impl.generator.AggregateSpecification)
     */
    public void addAggregateSpecification(AggregateSpecification spec, Position relativePosition, Class<AggregateSpecification> relativeSpec) {
        addSpec(this.aggregateSpecifications, spec, relativePosition, relativeSpec);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ma.glasnost.orika.impl.generator.CodeGenerationStrategy#
     * getAggregateSpecifications()
     */
    public List<AggregateSpecification> getAggregateSpecifications() {
        return aggregateSpecifications;
    }
    
}
