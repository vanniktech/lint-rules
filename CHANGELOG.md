# Change Log

Version 0.5.0 *(In development)*
--------------------------------

Version 0.4.0 *(2017-07-02)*
----------------------------

- Update Checkstyle to 7.8.2 [\#54](https://github.com/vanniktech/lint-rules/pull/54) ([vanniktech](https://github.com/vanniktech))
- Update PMD to 5.8.0 [\#53](https://github.com/vanniktech/lint-rules/pull/53) ([vanniktech](https://github.com/vanniktech))
- Don't generate BuildConfig file for release builds. [\#52](https://github.com/vanniktech/lint-rules/pull/52) ([vanniktech](https://github.com/vanniktech))
- Add WrongConstraintLayoutUsage. [\#50](https://github.com/vanniktech/lint-rules/pull/50) ([vanniktech](https://github.com/vanniktech))
- Fix a few more false positive cases. [\#49](https://github.com/vanniktech/lint-rules/pull/49) ([vanniktech](https://github.com/vanniktech))
- Add DefaultLayoutAttributeDetector. [\#48](https://github.com/vanniktech/lint-rules/pull/48) ([vanniktech](https://github.com/vanniktech))
- RawDimen: Ignore 0dp for constraint layouts in layout\_width & layout\_height. [\#47](https://github.com/vanniktech/lint-rules/pull/47) ([vanniktech](https://github.com/vanniktech))
- InvalidSingleLineCommentDetector: Further adjustments. [\#46](https://github.com/vanniktech/lint-rules/pull/46) ([vanniktech](https://github.com/vanniktech))
- Fix a few false / positives from upcoming 0.4.0 detectors. [\#45](https://github.com/vanniktech/lint-rules/pull/45) ([vanniktech](https://github.com/vanniktech))
- Add InvalidStringDetector. [\#43](https://github.com/vanniktech/lint-rules/pull/43) ([vanniktech](https://github.com/vanniktech))
- Add InvalidSingleLineCommentDetector. [\#42](https://github.com/vanniktech/lint-rules/pull/42) ([vanniktech](https://github.com/vanniktech))
- ShouldUseStaticImport: Add Collections.unmodifiable\* [\#41](https://github.com/vanniktech/lint-rules/pull/41) ([vanniktech](https://github.com/vanniktech))
- Add MatchingMenuIdDetector. [\#40](https://github.com/vanniktech/lint-rules/pull/40) ([vanniktech](https://github.com/vanniktech))
- Add MatchingViewIdDetector. [\#39](https://github.com/vanniktech/lint-rules/pull/39) ([vanniktech](https://github.com/vanniktech))
- Add a few more methods that should be statically imported. [\#37](https://github.com/vanniktech/lint-rules/pull/37) ([vanniktech](https://github.com/vanniktech))
- MethodMissingCheckReturnValue: Expand check to public io.reactivex package. [\#36](https://github.com/vanniktech/lint-rules/pull/36) ([vanniktech](https://github.com/vanniktech))
- Superfluous: Fix bug where margin + padding would be accumulated regardless of the parent. [\#34](https://github.com/vanniktech/lint-rules/pull/34) ([vanniktech](https://github.com/vanniktech))
- ShouldUseStaticImport: Don't crash on Method references. [\#33](https://github.com/vanniktech/lint-rules/pull/33) ([vanniktech](https://github.com/vanniktech))
- RawDimen: Also check for . in dp value, e.g. 0.5dp [\#32](https://github.com/vanniktech/lint-rules/pull/32) ([vanniktech](https://github.com/vanniktech))
- Let all Java Checks also run in Test Sources. [\#31](https://github.com/vanniktech/lint-rules/pull/31) ([vanniktech](https://github.com/vanniktech))
- RawDimen: Ignore 0dp layout\_widht & height when layout\_weight is set. [\#29](https://github.com/vanniktech/lint-rules/pull/29) ([vanniktech](https://github.com/vanniktech))
- RawDimen also execute on Drawables. [\#28](https://github.com/vanniktech/lint-rules/pull/28) ([vanniktech](https://github.com/vanniktech))
- Let Raw Detectors run on all attributes. [\#27](https://github.com/vanniktech/lint-rules/pull/27) ([vanniktech](https://github.com/vanniktech))
- Add ShouldUseStaticImportDetector. [\#24](https://github.com/vanniktech/lint-rules/pull/24) ([vanniktech](https://github.com/vanniktech))
- Add SuperfluousMarginDeclarationDetector & SuperfluousPaddingDeclarationDetector. [\#23](https://github.com/vanniktech/lint-rules/pull/23) ([vanniktech](https://github.com/vanniktech))

Version 0.3.0 *(2017-03-14)*
----------------------------

- Update Lint Version to 25.3.0 [\#21](https://github.com/vanniktech/lint-rules/pull/21) ([vanniktech](https://github.com/vanniktech))
- Handle Suppressing correctly in all Checks. [\#20](https://github.com/vanniktech/lint-rules/pull/20) ([vanniktech](https://github.com/vanniktech))
- Add WrongMenuIdFormat Check. [\#19](https://github.com/vanniktech/lint-rules/pull/19) ([vanniktech](https://github.com/vanniktech))
- RawDimen: Ignore tools. [\#18](https://github.com/vanniktech/lint-rules/pull/18) ([vanniktech](https://github.com/vanniktech))
- RawColor: Ignore tools. [\#17](https://github.com/vanniktech/lint-rules/pull/17) ([vanniktech](https://github.com/vanniktech))
- MethodMissingCheckReturnValue add TestObserver and TestSubscriber. [\#16](https://github.com/vanniktech/lint-rules/pull/16) ([vanniktech](https://github.com/vanniktech))

Version 0.2.0 *(2017-03-04)*
----------------------------

- Add RawColor Check. [\#12](https://github.com/vanniktech/lint-rules/pull/12) ([vanniktech](https://github.com/vanniktech))
- Add RawDimen Check. [\#11](https://github.com/vanniktech/lint-rules/pull/11) ([vanniktech](https://github.com/vanniktech))
- CompositeDisposable missing clear\(\) check. [\#9](https://github.com/vanniktech/lint-rules/pull/9) ([vanniktech](https://github.com/vanniktech))
- WrongIdFormatDetector [\#8](https://github.com/vanniktech/lint-rules/pull/8) ([vanniktech](https://github.com/vanniktech))
- Add Disposable to types that require @CheckReturnValue. Also fixed a bug. [\#6](https://github.com/vanniktech/lint-rules/pull/6) ([vanniktech](https://github.com/vanniktech))
- Add CheckReturnValue Lint Check [\#5](https://github.com/vanniktech/lint-rules/pull/5) ([vanniktech](https://github.com/vanniktech))

Version 0.1.0 *(2017-02-19)*
----------------------------

- Initial release