// Code generated by dagger-compiler.  Do not edit.
package sg.com.bigspoon.www.BGDashboard.core;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<UserAgentProvider>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code UserAgentProvider} and its
 * dependencies.
 * 
 * Being a {@code Provider<UserAgentProvider>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<UserAgentProvider>} and handling injection
 * of annotated fields.
 */
public final class UserAgentProvider$$InjectAdapter extends Binding<UserAgentProvider>
    implements Provider<UserAgentProvider>, MembersInjector<UserAgentProvider> {
  private Binding<android.content.pm.ApplicationInfo> appInfo;
  private Binding<android.content.pm.PackageInfo> info;
  private Binding<android.telephony.TelephonyManager> telephonyManager;
  private Binding<ClassLoader> classLoader;

  public UserAgentProvider$$InjectAdapter() {
    super("sg.com.bigspoon.www.BGDashboard.core.UserAgentProvider", "members/sg.com.bigspoon.www.BGDashboard.core.UserAgentProvider", NOT_SINGLETON, UserAgentProvider.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    appInfo = (Binding<android.content.pm.ApplicationInfo>) linker.requestBinding("android.content.pm.ApplicationInfo", UserAgentProvider.class);
    info = (Binding<android.content.pm.PackageInfo>) linker.requestBinding("android.content.pm.PackageInfo", UserAgentProvider.class);
    telephonyManager = (Binding<android.telephony.TelephonyManager>) linker.requestBinding("android.telephony.TelephonyManager", UserAgentProvider.class);
    classLoader = (Binding<ClassLoader>) linker.requestBinding("java.lang.ClassLoader", UserAgentProvider.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(appInfo);
    injectMembersBindings.add(info);
    injectMembersBindings.add(telephonyManager);
    injectMembersBindings.add(classLoader);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<UserAgentProvider>}.
   */
  @Override
  public UserAgentProvider get() {
    UserAgentProvider result = new UserAgentProvider();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<UserAgentProvider>}.
   */
  @Override
  public void injectMembers(UserAgentProvider object) {
    object.appInfo = appInfo.get();
    object.info = info.get();
    object.telephonyManager = telephonyManager.get();
    object.classLoader = classLoader.get();
  }
}
