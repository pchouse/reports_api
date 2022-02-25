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

package pt.pchouse.reports.api.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MetadataTest extends Generic<Metadata> {

    @Test
    @Override
    public void testAnnotations() {
        assertThat(Metadata.class.isAnnotationPresent(Component.class)).isTrue();
        assertThat(Metadata.class.isAnnotationPresent(Scope.class)).isTrue();
        assertThat(
                Metadata.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();
    }

    @Test
    public void testBean(){
        Metadata metadata = appContext.getBean(Metadata.class);

        String title = "The title";
        String author = "The author";
        String subject = "The subject";
        String keywords = "The keywords";
        String application = "The application";
        String creator = "The creator";

        metadata.setTitle(title);
        metadata.setAuthor(author);
        metadata.setSubject(subject);
        metadata.setKeywords(keywords);
        metadata.setApplication(application);
        metadata.setCreator(creator);

        assertThat(metadata.getTitle()).isEqualTo(title);
        assertThat(metadata.getAuthor()).isEqualTo(author);
        assertThat( metadata.getSubject()).isEqualTo(subject);
        assertThat(metadata.getKeywords()).isEqualTo(keywords);
        assertThat(metadata.getApplication()).isEqualTo(application);
        assertThat(metadata.getCreator()).isEqualTo(creator);

        for(boolean bool : new boolean[]{true, false, true}){
            metadata.setDisplayMetadataTitle(bool);
            assertThat(metadata.isDisplayMetadataTitle()).isEqualTo(bool);
        }
    }

}
