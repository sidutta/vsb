package eu.chorevolution.vsb.gm.protocols.soap;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.ws.Endpoint;

import eu.chorevolution.vsb.gm.protocols.primitives.BcGmComponent;
import eu.chorevolution.vsb.gmdl.utils.BcConfiguration;
import eu.chorevolution.vsb.gmdl.utils.Data;

public class BcSoapComponent extends BcGmComponent {

  private Endpoint endpoint;

  public BcSoapComponent(BcConfiguration bcConfiguration) {
    super(bcConfiguration);
  }

  @Override
  public void start() {
    switch (this.bcConfiguration.getComponentRole()) {
    case "SERVER":
      Class<?> bc = null;
      try {
        bc = Class.forName(this.bcConfiguration.getTargetNamespace()+"."+this.bcConfiguration.getServiceName());
      } catch (ClassNotFoundException e1) {
        e1.printStackTrace();
      }
      try {
        this.endpoint = Endpoint.publish(this.bcConfiguration.getComponentAddress(), bc.getDeclaredConstructor(BcGmComponent.class).newInstance(this));
        System.err.println("SOAP endpoint published on " + this.bcConfiguration.getComponentAddress());
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      }
      break;
    case "CLIENT":
      
      break;
    default:
      break;
    }
  }
  
  @Override
  public void stop() {
    if(this.endpoint.isPublished()) {
      this.endpoint.stop();
    }
  }
  
  @Override
  public void postOneway(final String destination, final String scope, final List<Data<?>> data, final long lease) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void mgetOneway(final String scope, final Object exchange) {
    this.nextComponent.postOneway(this.bcConfiguration.getServiceAddress(), scope, (List<Data<?>>)exchange, 0);
  }
  
  @Override
  public void xmgetOneway(final String source, final String scope, final Object exchange) {
    this.nextComponent.postOneway(this.bcConfiguration.getServiceAddress(), scope, (List<Data<?>>)exchange, 0);
  }
  
  @Override
  public <T> T postTwowaySync(final String destination, final String scope, final List<Data<?>> datas, final long lease) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void xtgetTwowaySync(final String destination, final String scope, final long timeout, final Object response) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public <T> T mgetTwowaySync(final String scope, final Object exchange) {
    return this.nextComponent.postTwowaySync(this.bcConfiguration.getServiceAddress(), scope, (List<Data<?>>)exchange, 0);
  }
  
  @Override
  public void postTwowayAsync(final String destination, final String scope, final List<Data<?>> data, final long lease) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void xgetTwowayAsync(final String destination, final String scope, final Object response) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void mgetTwowayAsync(final String scope, final Object exchange) {
    this.nextComponent.postTwowayAsync(this.bcConfiguration.getServiceAddress(), scope, (List<Data<?>>)exchange, 0);
  }
  
  @Override
  public void postBackTwowayAsync(final String source, final String scope, final Data<?> data, final long lease, final Object exchange) {
    // TODO Auto-generated method stub
  }
}