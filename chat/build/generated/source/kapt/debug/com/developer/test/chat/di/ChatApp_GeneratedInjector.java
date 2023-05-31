package com.developer.test.chat.di;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = ChatApp.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface ChatApp_GeneratedInjector {
  void injectChatApp(ChatApp chatApp);
}
