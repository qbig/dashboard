// Code generated by dagger-compiler.  Do not edit.
package sg.com.bigspoon.www.BGDashboard.ui;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<NavigationDrawerFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code NavigationDrawerFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<NavigationDrawerFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<NavigationDrawerFragment>} and handling injection
 * of annotated fields.
 */
public final class NavigationDrawerFragment$$InjectAdapter extends Binding<NavigationDrawerFragment>
    implements Provider<NavigationDrawerFragment>, MembersInjector<NavigationDrawerFragment> {
  private Binding<android.content.SharedPreferences> prefs;
  private Binding<com.squareup.otto.Bus> bus;

  public NavigationDrawerFragment$$InjectAdapter() {
    super("sg.com.bigspoon.www.BGDashboard.ui.NavigationDrawerFragment", "members/sg.com.bigspoon.www.BGDashboard.ui.NavigationDrawerFragment", NOT_SINGLETON, NavigationDrawerFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    prefs = (Binding<android.content.SharedPreferences>) linker.requestBinding("android.content.SharedPreferences", NavigationDrawerFragment.class);
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", NavigationDrawerFragment.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(prefs);
    injectMembersBindings.add(bus);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<NavigationDrawerFragment>}.
   */
  @Override
  public NavigationDrawerFragment get() {
    NavigationDrawerFragment result = new NavigationDrawerFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<NavigationDrawerFragment>}.
   */
  @Override
  public void injectMembers(NavigationDrawerFragment object) {
    object.prefs = prefs.get();
    object.bus = bus.get();
  }
}
