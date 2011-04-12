package annis.service.internal;

import annis.WekaHelper;
import annis.resolver.ResolverEntry;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import annis.dao.AnnisDao;
import annis.dao.AnnotatedMatch;
import annis.exceptions.AnnisBinaryNotFoundException;
import annis.exceptions.AnnisCorpusAccessException;
import annis.exceptions.AnnisQLSemanticsException;
import annis.exceptions.AnnisQLSyntaxException;
import annis.externalFiles.ExternalFileMgr;
import annis.model.Annotation;
import annis.model.AnnotationGraph;
import annis.resolver.SingleResolverRequest;
import annis.service.AnnisService;
import annis.service.AnnisServiceException;
import annis.service.ifaces.AnnisAttributeSet;
import annis.service.ifaces.AnnisBinary;
import annis.service.ifaces.AnnisCorpusSet;
import annis.service.ifaces.AnnisResult;
import annis.service.ifaces.AnnisResultSet;
import annis.service.objects.AnnisAttributeSetImpl;
import annis.service.objects.AnnisCorpusSetImpl;
import annis.service.objects.AnnisResultImpl;
import annis.service.objects.AnnisResultSetImpl;

// TODO: Exceptions aufräumen
// TODO: TestCase fehlt
public class AnnisServiceImpl implements AnnisService
{

  private static final long serialVersionUID = 1970615866336637980L;
  private Logger log = Logger.getLogger(this.getClass());
  private AnnisDao annisDao;
  private ExternalFileMgr externalFileMgr;
  private WekaHelper wekaHelper;

  /**
   * Log the successful initialization of this bean.
   *
   * <p>
   * XXX: This should be a private method annotated with <tt>@PostConstruct</tt>, but
   * that doesn't seem to work.  As a work-around, the method is called
   * by Spring as an init-method.
   */
  public void sayHello()
  {
    // log a message after successful startup
    log.info("AnnisService loaded.");
  }

  @Override
  public void ping() throws RemoteException
  {
  }


  @Override
  public int getCount(List<Long> corpusList, String annisQuery) throws RemoteException, AnnisQLSemanticsException
  {
    return annisDao.countMatches(corpusList, annisDao.parseAQL(annisQuery, corpusList));
  }

  @Override
  public AnnisResultSet getResultSet(List<Long> corpusList, String annisQuery, int limit, int offset, int contextLeft, int contextRight)
    throws RemoteException, AnnisQLSemanticsException, AnnisQLSyntaxException, AnnisCorpusAccessException
  {
    List<AnnotationGraph> annotationGraphs = annisDao.retrieveAnnotationGraph(corpusList, annisDao.parseAQL(annisQuery, corpusList), offset, limit, contextLeft, contextRight);
    AnnisResultSetImpl annisResultSet = new AnnisResultSetImpl();
    for(AnnotationGraph annotationGraph : annotationGraphs)
    {
      annisResultSet.add(new AnnisResultImpl(annotationGraph));
    }
    return annisResultSet;
  }

  @Override
  public AnnisCorpusSet getCorpusSet() throws RemoteException
  {
    return new AnnisCorpusSetImpl(annisDao.listCorpora());
  }

  @Override
  public AnnisAttributeSet getNodeAttributeSet(List<Long> corpusList, boolean fetchValues) throws RemoteException
  {
    return new AnnisAttributeSetImpl(annisDao.listNodeAnnotations(corpusList, fetchValues));
  }

  @Override
  public String getPaula(Long textId) throws RemoteException
  {
    AnnotationGraph graph = annisDao.retrieveAnnotationGraph(textId);
    if(graph != null)
    {
      return new AnnisResultImpl(graph).getPaula();
    }
    throw new AnnisServiceException("no text found with id = " + textId);
  }

  @Override
  public AnnisResult getAnnisResult(Long textId) throws RemoteException
  {
    AnnotationGraph graph = annisDao.retrieveAnnotationGraph(textId);
    if(graph != null)
    {
      return new AnnisResultImpl(graph);
    }
    throw new AnnisServiceException("no text found with id = " + textId);
  }

  @Override
  public boolean isValidQuery(String annisQuery) throws RemoteException, AnnisQLSemanticsException, AnnisQLSyntaxException
  {
    annisDao.parseAQL(annisQuery, null);
    return true;
  }

  // TODO: test getBinary
  @Override
  public AnnisBinary getBinary(Long id) throws AnnisBinaryNotFoundException
  {
    log.debug("Retrieving binary file with id = " + id);

    try
    {
      return externalFileMgr.getBinary(id);
    }
    catch(Exception e)
    {
      throw new AnnisBinaryNotFoundException(e.getMessage());
    }
  }

  @Override
  public List<Annotation> getMetadata(long corpusId) throws RemoteException, AnnisServiceException
  {
    return annisDao.listCorpusAnnotations(corpusId);
  }

  @Override
  public List<ResolverEntry> getResolverEntries(SingleResolverRequest[] request)
    throws RemoteException
  {
    return annisDao.getResolverEntries(request);
  }


  @Override
  public String getWeka(List<Long> corpusList, String annisQL) throws RemoteException, AnnisQLSemanticsException, AnnisQLSyntaxException, AnnisCorpusAccessException
  {
    List<AnnotatedMatch> matches = annisDao.matrix(corpusList, annisDao.parseAQL(annisQL, corpusList));
    if(matches.isEmpty())
    {
      return "(empty)";
    }
    else
    {
      return wekaHelper.exportAsArff(matches);
    }
  }

  ///// Getter / Setter
  public ExternalFileMgr getExternalFileMgr()
  {
    return externalFileMgr;
  }

  public void setExternalFileMgr(ExternalFileMgr externalFileMgr)
  {
    this.externalFileMgr = externalFileMgr;
  }

  public AnnisDao getAnnisDao()
  {
    return annisDao;
  }

  public void setAnnisDao(AnnisDao annisDao)
  {
    this.annisDao = annisDao;
  }

  public WekaHelper getWekaHelper()
  {
    return wekaHelper;
  }

  public void setWekaHelper(WekaHelper wekaHelper)
  {
    this.wekaHelper = wekaHelper;
  }
}
