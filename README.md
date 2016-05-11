# Android

## Ferramentas

   * Android Studio 2.1
   * Android SDK
   * Source Tree

## Ambiente de desenvolvimento

## Referência API
http://api-hck.hotmart.com/hack-swagger/dist/index.html

### ~/.gradle/gradle.properties

Utilizamos variáveis de ambiente globais a fim de parametrizar nossas referências a recursos sensíveis. Uma delas é nossa KeyStore que armazena nossa chave de assinatura para publicarmos nossos Apps na Google Play Store.
Se seu projeto não compila por razão de um `hotmart_releaseStoreFile` ou similar, você precisa adicionar essas variáveis ao seu ambiente.
Para tal, adicione ao seu diretório `~/.gradle` o arquivo `gradle.properties` [(download aqui)](https://github.com/Hotmart-Org/hack-app/blob/master/extras/gradle.properties), ou, se ele já existir, inclua as seguintes linhas:

```
hotmart_releaseStoreFile=/dev/null
hotmart_releaseStorePassword=/dev/null
hotmart_releaseKeyAlias=/dev/null
hotmart_releaseKeyPassword=/dev/null
```

### Configurações de Formatação de Código e tipos de arquivos
Utilizamos os seguintes padrões:

   * Delimitador de fim de linha: UNIX (LF ou `\n`)
   * Codificação de arquivos: UTF-8
   * Indentação: 4 espaços
   
A fim de garantir uma melhor qualidade do nosso código-fonte, estamos cedendo um arquivo de configuração para o Android Studio, onde essas e várias outras preferências estão definidas.

Para importá-las:

   1. baixe o pacote de configurações do Android Studio [(android-studio-settings.jar)](https://github.com/Hotmart-Org/hack-app/blob/master/extras/android-studio-settings.jar)
   2. abra o Android Studio
   3. no menu `Configure`, selecione `Import Settings`
   4. navegue até o diretório onde baixou o android-studio-settings.jar
   5. selecione todas as opções disponíveis
   6. reinicie o Android Studio

**Obs. 1:** Talvez ele perca as referências do diretório do JDK e do Android SDK. Apenas aponte as referências corretas nas telas que seguem.

**Obs. 2:** Este pacote de configurações inclui alguns plugins bastante úteis. Ao reiniciar, o Android Studio irá solicitar um download deles. Recomendamos que o faça.

### gradle.properties
Observe que utilizamos o gradle.properties para gerenciar o número de versão de algumas dependências, como as bibliotecas android-support e play-services. Isso nos ajuda a manter todas as dependências nas versões corretas. 

## Arquitetura

### com.hotmart.dragonfly.ui
   * BaseActivity
   * CollectionRecyclerViewAdapter
   * PageableLoader

## Dependências
    
### [ButterKnife 8.0.0](http://jakewharton.github.io/butterknife/)
Facilita o trabalho de encontrar e ligar Views com membros de uma classe, sem a necessidade de se fazer casts e `findViewById(int)`, utilizando anotações.

### [Gson 2.6.2](https://github.com/google/gson)
Serialização e desserialização de POJOs em formato JSON.

### [Retrofit 2.0.2](http://square.github.io/retrofit/)
Um cliente HTTP para Java e Android que facilita a escrita de chamadas de APIs RESTful.

### [Android Saripaar 2.0.3](https://github.com/ragunathjawahar/android-saripaar)
Validação de formulários de forma limpa e simples.

### [Android Support 23.3.0](http://developer.android.com/intl/pt-br/tools/support-library/index.html)
Bibliotecas da Google que oferecem retrocompatibilidade de versões do Android. Neste projeto, estamos utilizando:

   * appcompat-v7
   * design
   * recyclerview-v7

### [Google Play Services 8.4.0](https://developers.google.com/android/guides/setup)

   * play-services-maps: Google Maps
   * play-services-location: Google Location, Activity Recognition, and Places
   * play-services-identity: Google Address API

## Hierarquia de temas
