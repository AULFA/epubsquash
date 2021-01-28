/*
 * Copyright © 2019 Library For All
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package one.lfa.epubsquash.vanilla;

import one.lfa.epubsquash.api.EPUBSquasherConfiguration;
import one.lfa.epubsquash.api.EPUBSquasherProviderType;
import one.lfa.epubsquash.api.EPUBSquasherType;
import one.lfa.epubsquash.vanilla.internal.EPUBSquasher;

/**
 * A provider of EPUB squashers.
 */

public final class EPUBSquashers implements EPUBSquasherProviderType
{
  /**
   * Construct a provider.
   */

  public EPUBSquashers()
  {

  }

  @Override
  public EPUBSquasherType createSquasher(
    final EPUBSquasherConfiguration configuration)
  {
    return new EPUBSquasher(configuration);
  }
}
