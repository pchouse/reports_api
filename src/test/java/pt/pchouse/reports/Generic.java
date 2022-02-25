/*
 *  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package pt.pchouse.reports;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class Generic<T> {

    @Autowired
    protected ApplicationContext appContext;

    /**
     * @return The generic class
     * @link https://stackoverflow.com/a/57833645/6397645
     */
    @SuppressWarnings("rawtypes")
    public Class getTypeClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        try {
            return Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    @Test
    public void testInstance() {
        @SuppressWarnings("unchecked") T instance = (T) appContext.getBean(getTypeClass());
        assertThat(instance).isInstanceOf(getTypeClass());
    }

    @Test
    public void testAnnotations() {
        //noinspection unchecked
        assertThat(getTypeClass().isAnnotationPresent(Component.class)).isTrue();
        assertThat(getTypeClass().isAnnotationPresent(Scope.class)).isTrue();
    }
}
